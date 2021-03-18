package com.storetemplate.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.bj.dto.es.SkuEsModel;
import com.bj.utils.R;
import com.storetemplate.search.config.EsConfig;
import com.storetemplate.search.constant.EsConstant;
import com.storetemplate.search.feign.ProductFeignService;
import com.storetemplate.search.service.SearchIndexService;
import com.bj.dto.BrandTo;
import com.storetemplate.search.vo.SearchParam;
import com.storetemplate.search.vo.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName SearchIndexServiceImpl
 * @Description TODO
 * @Author 13011
 * @Date 2020/11/12 16:46
 * @Version 1.0
 **/
@Slf4j
@Service
public class SearchIndexServiceImpl implements SearchIndexService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private ProductFeignService productFeignService;

    @Override
    public SearchResult searchIndex(SearchParam searchParam) {
        SearchResult searchResult = new SearchResult();
        SearchRequest searchRequest = buildSearchRequest(searchParam);
        try {
            SearchResponse response = restHighLevelClient.search(searchRequest, EsConfig.COMMOM_OPTIONS);
            searchResult = buildSearchResult(response, searchParam);
        } catch (IOException e) {
            log.error("es搜索出错");
            e.printStackTrace();
        }
        return searchResult;
    }

    /**
     * 准备检索请求  [构建查询语句]
     */
    private SearchRequest buildSearchRequest(SearchParam searchParam) {
        /*
         * 检索条件应包含模糊匹配关键字，过滤（属性，分类，品牌，价格区间，库存），排序，分页，高亮，聚合分析
         * */
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();//dsl构建对象
        //1.构建 bool对象
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //构建模糊匹配关键字
        if (!StringUtils.isEmpty(searchParam.getKeyword())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("skuTitle", searchParam.getKeyword()));
        }
        //2.bool filter过滤
        if (searchParam.getCatalog3Id() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("catalogId", searchParam.getCatalog3Id()));
        }
        //3.按照品牌id数组查询
        if (!CollectionUtils.isEmpty(searchParam.getBrandId())) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId", searchParam.getBrandId()));
        }
        //4.按照指定属性查询
        if (!CollectionUtils.isEmpty(searchParam.getAttrs())) {
            for (String paramAttr : searchParam.getAttrs()) {
                BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
                String[] s = paramAttr.split("_");
                String attrId = s[0];
                String[] attrValues = s[1].split(":");
                boolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                boolQuery.must(QueryBuilders.termsQuery("attrs.attrValue", attrValues));
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", boolQuery, ScoreMode.None);
                boolQueryBuilder.filter(nestedQuery);
            }
        }
        //5.按照是否有货进行查询
        if (searchParam.getHasStock() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("hasStock", searchParam.getHasStock() == 1 ? true : false));
        }
        //6.按照价格区间过滤
        if (!StringUtils.isEmpty(searchParam.getSkuPrice())) {
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
            String[] s = searchParam.getSkuPrice().split("_");
            if (s.length == 2) {
                //传来的是有区间的价格
                rangeQuery.gte(s[0]).lte(s[1]);
            } else if (s.length == 1) {
                if (searchParam.getSkuPrice().startsWith("_")) {
                    rangeQuery.lte(s[0]);
                }
                if (searchParam.getSkuPrice().endsWith("_")) {
                    rangeQuery.gte(s[0]);
                }
            }
            boolQueryBuilder.filter(rangeQuery);
        }
        sourceBuilder.query(boolQueryBuilder);
        //7.是否排序
        if (!StringUtils.isEmpty(searchParam.getSort())) {
            String[] s = searchParam.getSort().split("_");
            SortOrder sortOrder = s[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC;
            sourceBuilder.sort(s[0], sortOrder);
        }
        //8.分页
        sourceBuilder.from((searchParam.getPageNum() - 1) * 3);
        sourceBuilder.size(3);
        ////9.高亮
        if (!StringUtils.isEmpty(searchParam.getKeyword())) {
            HighlightBuilder builder = new HighlightBuilder();
            builder.field("skuTitle");
            builder.preTags("<b style='color:red'>");
            builder.postTags("</b>");
            sourceBuilder.highlighter(builder);
        }
        /*
         * 聚合部分
         * */
        //1.品牌聚合
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg");
        brand_agg.field("brandId").size(50);
        //1.1品牌聚合子聚合
        brand_agg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(1));
        brand_agg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1));
        sourceBuilder.aggregation(brand_agg);

        //2.分类聚合
        TermsAggregationBuilder catalog_agg = AggregationBuilders.terms("catalog_agg");
        catalog_agg.field("catalogId").size(200);
        catalog_agg.subAggregation(AggregationBuilders.terms("catalog_name_agg").field("catalogName").size(1));
        sourceBuilder.aggregation(catalog_agg);

        //3.属性聚合
        NestedAggregationBuilder attr_agg = AggregationBuilders.nested("attr_agg", "attrs");
        TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId").size(20);
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(20));
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1));
        attr_agg.subAggregation(attr_id_agg);
        sourceBuilder.aggregation(attr_agg);
        System.out.println("构建的dsl:" + sourceBuilder.toString());
        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, sourceBuilder);
        return searchRequest;

    }

    /**
     * 构建结果数据
     */
    private SearchResult buildSearchResult(SearchResponse response, SearchParam Param) {
        SearchResult result = new SearchResult();
        // 1.返回的所有查询到的商品
        SearchHits hits = response.getHits();

        List<SkuEsModel> esModels = new ArrayList<>();
        if (hits.getHits() != null && hits.getHits().length > 0) {
            for (SearchHit hit : hits.getHits()) {
                String sourceAsString = hit.getSourceAsString();
                // ES中检索得到的对象
                SkuEsModel esModel = JSON.parseObject(sourceAsString, SkuEsModel.class);
                if (!StringUtils.isEmpty(Param.getKeyword())) {
                    // 1.1 获取标题的高亮属性
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    String highlightFields = skuTitle.getFragments()[0].string();
                    // 1.2 设置文本高亮
                    esModel.setSkuTitle(highlightFields);
                }
                esModels.add(esModel);
            }
        }
        result.setProducts(esModels);
        /*
         * 获取聚合信息
         * */
        // 2.当前所有商品涉及到的所有属性信息
        ArrayList<SearchResult.AttrVo> attrVos = new ArrayList<>();
        ParsedNested attr_agg = response.getAggregations().get("attr_agg");
        ParsedLongTerms attr_id = attr_agg.getAggregations().get("attr_id_agg");
        for (Terms.Bucket bucket : attr_id.getBuckets()) {
            SearchResult.AttrVo attrVo = new SearchResult.AttrVo();
            // 2.1 得到属性的id
            attrVo.setAttrId(bucket.getKeyAsNumber().longValue());
            // 2.2 得到属性的名字
            String attr_name = ((ParsedStringTerms) bucket.getAggregations().get("attr_name_agg")).getBuckets().get(0).getKeyAsString();
            attrVo.setAttrName(attr_name);
            // 2.3 得到属性的所有值
            List<String> attr_value = ((ParsedStringTerms) bucket.getAggregations().get("attr_value_agg")).getBuckets().stream().map(item -> item.getKeyAsString()).collect(Collectors.toList());
            attrVo.setAttrValue(attr_value);
            attrVos.add(attrVo);
        }
        result.setAttrVos(attrVos);

        // 3.当前所有商品涉及到的所有品牌信息
        ArrayList<SearchResult.BrandVo> brandVos = new ArrayList<>();
        ParsedLongTerms brand_agg = response.getAggregations().get("brand_agg");
        for (Terms.Bucket bucket : brand_agg.getBuckets()) {
            SearchResult.BrandVo brandVo = new SearchResult.BrandVo();
            // 3.1 得到品牌的id
            long brnadId = bucket.getKeyAsNumber().longValue();
            brandVo.setBrandId(brnadId);
            // 3.2 得到品牌的名
            String brand_name = ((ParsedStringTerms) bucket.getAggregations().get("brand_name_agg")).getBuckets().get(0).getKeyAsString();
            brandVo.setBrandName(brand_name);
            // 3.3 得到品牌的图片
            String brand_img = ((ParsedStringTerms) (bucket.getAggregations().get("brand_img_agg"))).getBuckets().get(0).getKeyAsString();
            brandVo.setBrandImg(brand_img);
            brandVos.add(brandVo);
        }
        result.setBrands(brandVos);

        // 4.当前商品所有涉及到的分类信息
        ParsedLongTerms catalog_agg = response.getAggregations().get("catalog_agg");
        List<SearchResult.CatalogVo> catalogVos = new ArrayList<>();
        for (Terms.Bucket bucket : catalog_agg.getBuckets()) {
            // 设置分类id
            SearchResult.CatalogVo catalogVo = new SearchResult.CatalogVo();
            catalogVo.setCatalogId(Long.parseLong(bucket.getKeyAsString()));
            // 得到分类名
            ParsedStringTerms catalog_name_agg = bucket.getAggregations().get("catalog_name_agg");
            String catalog_name = catalog_name_agg.getBuckets().get(0).getKeyAsString();
            catalogVo.setCatalogName(catalog_name);
            catalogVos.add(catalogVo);
        }
        result.setCatalogVos(catalogVos);

        // 5.分页信息-页码
        result.setPageNum(Param.getPageNum());

        // 总记录数
        long total = hits.getTotalHits().value;

        result.setTotal((int) total);
        // 总页码：计算得到
        int totalPages = (int) (total / 3 + (total % 3 == 0 ? 0 : 1));
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            list.add(i);
        }
        result.setPageNavs(list);
        result.setTotalPages(totalPages);
        // 6.构建面包屑导航功能
        if (Param.getAttrs() != null) {
            List<SearchResult.NavVo> navVos = Param.getAttrs().stream().map(attr -> {
                SearchResult.NavVo navVo = new SearchResult.NavVo();
                String[] s = attr.split("_");
                navVo.setNavValue(s[1]);
                R r = productFeignService.info(Long.parseLong(s[0]));
                // 将已选择的请求参数添加进去 前端页面进行排除
                result.getAttrIds().add(Long.parseLong(s[0]));
                String code = String.valueOf(r.get("code"));
                if ("0".equalsIgnoreCase(code)) {
                    Map<String, Object> data = (Map<String, Object>) r.get("attr");
                    if (data != null) {
                        String attrName = (String) data.get("attrName");
                        navVo.setName(attrName);
                    }
                } else {
                    // 失败了就拿id作为名字
                    navVo.setName(s[0]);
                }
                // 拿到所有查询条件 替换查询条件
                String replace = replaceQueryString(Param, attr, "attrs");
                navVo.setLink("http://search.storeTemplate.com/index.html?" + replace);
                return navVo;
            }).collect(Collectors.toList());
            result.setNavs(navVos);
        }

        // 品牌
        if (Param.getBrandId() != null && Param.getBrandId().size() > 0) {
            List<SearchResult.NavVo> navs = result.getNavs();
            SearchResult.NavVo navVo = new SearchResult.NavVo();
            navVo.setName("品牌:");
            // TODO 远程查询所有品牌
            List<BrandTo> brandInfos = productFeignService.brandInfo(Param.getBrandId());
            StringBuffer buffer = new StringBuffer();
            // 替换所有品牌ID
            String replace = "";
            String brandNames="";
            if (!CollectionUtils.isEmpty(brandInfos)) {
                int i = 0;
                for (BrandTo brandTo : brandInfos) {
                    buffer.append(brandTo.getBrandName() + ";");
                    i++;
                    replace = replaceQueryString(Param, brandTo.getBrandId() + "", "brandId");
                }
                if(i==1){
                    brandNames=buffer.toString().replace(";","");
                }
                else brandNames=buffer.toString();
            }
            navVo.setNavValue(brandNames);
            navVo.setLink("http://search.storeTemplate.com/index.html?" + replace);
            navs.add(navVo);
        }
        return result;
    }

    /**
     * 替换字符
     * key ：需要替换的key
     */
    private String replaceQueryString(SearchParam Param, String value, String key) {
        String encode = null;
        try {
            encode = URLEncoder.encode(value, "UTF-8");
            // 浏览器对空格的编码和java的不一样
            encode = encode.replace("+", "%20");
            encode = encode.replace("%28", "(").replace("%29", ")");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Param.get_queryString().replace("&" + key + "=" + encode, "");
    }


}