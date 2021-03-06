package com.storetemplate.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.common.to.BrandTo;
import com.atguigu.common.to.es.SkuEsModel;
import com.atguigu.common.utils.R;
import com.storetemplate.search.config.EsConfig;
import com.storetemplate.search.constant.EsConstant;
import com.storetemplate.search.feign.ProductFeignService;
import com.storetemplate.search.service.SearchIndexService;
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
            log.error("es????????????");
            e.printStackTrace();
        }
        return searchResult;
    }

    /**
     * ??????????????????  [??????????????????]
     */
    private SearchRequest buildSearchRequest(SearchParam searchParam) {
        /*
         * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
         * */
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();//dsl????????????
        //1.?????? bool??????
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //???????????????????????????
        if (!StringUtils.isEmpty(searchParam.getKeyword())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("skuTitle", searchParam.getKeyword()));
        }
        //2.bool filter??????
        if (searchParam.getCatalog3Id() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("catalogId", searchParam.getCatalog3Id()));
        }
        //3.????????????id????????????
        if (!CollectionUtils.isEmpty(searchParam.getBrandId())) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId", searchParam.getBrandId()));
        }
        //4.????????????????????????
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
        //5.??????????????????????????????
        if (searchParam.getHasStock() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("hasStock", searchParam.getHasStock() == 1 ? true : false));
        }
        //6.????????????????????????
        if (!StringUtils.isEmpty(searchParam.getSkuPrice())) {
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
            String[] s = searchParam.getSkuPrice().split("_");
            if (s.length == 2) {
                //??????????????????????????????
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
        //7.????????????
        if (!StringUtils.isEmpty(searchParam.getSort())) {
            String[] s = searchParam.getSort().split("_");
            SortOrder sortOrder = s[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC;
            sourceBuilder.sort(s[0], sortOrder);
        }
        //8.??????
        sourceBuilder.from((searchParam.getPageNum() - 1) * 3);
        sourceBuilder.size(3);
        ////9.??????
        if (!StringUtils.isEmpty(searchParam.getKeyword())) {
            HighlightBuilder builder = new HighlightBuilder();
            builder.field("skuTitle");
            builder.preTags("<b style='color:red'>");
            builder.postTags("</b>");
            sourceBuilder.highlighter(builder);
        }
        /*
         * ????????????
         * */
        //1.????????????
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg");
        brand_agg.field("brandId").size(50);
        //1.1?????????????????????
        brand_agg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(1));
        brand_agg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1));
        sourceBuilder.aggregation(brand_agg);

        //2.????????????
        TermsAggregationBuilder catalog_agg = AggregationBuilders.terms("catalog_agg");
        catalog_agg.field("catalogId").size(200);
        catalog_agg.subAggregation(AggregationBuilders.terms("catalog_name_agg").field("catalogName").size(1));
        sourceBuilder.aggregation(catalog_agg);

        //3.????????????
        NestedAggregationBuilder attr_agg = AggregationBuilders.nested("attr_agg", "attrs");
        TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId").size(20);
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(20));
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1));
        attr_agg.subAggregation(attr_id_agg);
        sourceBuilder.aggregation(attr_agg);
        System.out.println("?????????dsl:" + sourceBuilder.toString());
        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, sourceBuilder);
        return searchRequest;

    }

    /**
     * ??????????????????
     */
    private SearchResult buildSearchResult(SearchResponse response, SearchParam Param) {
        SearchResult result = new SearchResult();
        // 1.?????????????????????????????????
        SearchHits hits = response.getHits();

        List<SkuEsModel> esModels = new ArrayList<>();
        if (hits.getHits() != null && hits.getHits().length > 0) {
            for (SearchHit hit : hits.getHits()) {
                String sourceAsString = hit.getSourceAsString();
                // ES????????????????????????
                SkuEsModel esModel = JSON.parseObject(sourceAsString, SkuEsModel.class);
                if (!StringUtils.isEmpty(Param.getKeyword())) {
                    // 1.1 ???????????????????????????
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    String highlightFields = skuTitle.getFragments()[0].string();
                    // 1.2 ??????????????????
                    esModel.setSkuTitle(highlightFields);
                }
                esModels.add(esModel);
            }
        }
        result.setProducts(esModels);
        /*
         * ??????????????????
         * */
        // 2.????????????????????????????????????????????????
        ArrayList<SearchResult.AttrVo> attrVos = new ArrayList<>();
        ParsedNested attr_agg = response.getAggregations().get("attr_agg");
        ParsedLongTerms attr_id = attr_agg.getAggregations().get("attr_id_agg");
        for (Terms.Bucket bucket : attr_id.getBuckets()) {
            SearchResult.AttrVo attrVo = new SearchResult.AttrVo();
            // 2.1 ???????????????id
            attrVo.setAttrId(bucket.getKeyAsNumber().longValue());
            // 2.2 ?????????????????????
            String attr_name = ((ParsedStringTerms) bucket.getAggregations().get("attr_name_agg")).getBuckets().get(0).getKeyAsString();
            attrVo.setAttrName(attr_name);
            // 2.3 ????????????????????????
            List<String> attr_value = ((ParsedStringTerms) bucket.getAggregations().get("attr_value_agg")).getBuckets().stream().map(item -> item.getKeyAsString()).collect(Collectors.toList());
            attrVo.setAttrValue(attr_value);
            attrVos.add(attrVo);
        }
        result.setAttrVos(attrVos);

        // 3.????????????????????????????????????????????????
        ArrayList<SearchResult.BrandVo> brandVos = new ArrayList<>();
        ParsedLongTerms brand_agg = response.getAggregations().get("brand_agg");
        for (Terms.Bucket bucket : brand_agg.getBuckets()) {
            SearchResult.BrandVo brandVo = new SearchResult.BrandVo();
            // 3.1 ???????????????id
            long brnadId = bucket.getKeyAsNumber().longValue();
            brandVo.setBrandId(brnadId);
            // 3.2 ??????????????????
            String brand_name = ((ParsedStringTerms) bucket.getAggregations().get("brand_name_agg")).getBuckets().get(0).getKeyAsString();
            brandVo.setBrandName(brand_name);
            // 3.3 ?????????????????????
            String brand_img = ((ParsedStringTerms) (bucket.getAggregations().get("brand_img_agg"))).getBuckets().get(0).getKeyAsString();
            brandVo.setBrandImg(brand_img);
            brandVos.add(brandVo);
        }
        result.setBrands(brandVos);

        // 4.??????????????????????????????????????????
        ParsedLongTerms catalog_agg = response.getAggregations().get("catalog_agg");
        List<SearchResult.CatalogVo> catalogVos = new ArrayList<>();
        for (Terms.Bucket bucket : catalog_agg.getBuckets()) {
            // ????????????id
            SearchResult.CatalogVo catalogVo = new SearchResult.CatalogVo();
            catalogVo.setCatalogId(Long.parseLong(bucket.getKeyAsString()));
            // ???????????????
            ParsedStringTerms catalog_name_agg = bucket.getAggregations().get("catalog_name_agg");
            String catalog_name = catalog_name_agg.getBuckets().get(0).getKeyAsString();
            catalogVo.setCatalogName(catalog_name);
            catalogVos.add(catalogVo);
        }
        result.setCatalogVos(catalogVos);

        // 5.????????????-??????
        result.setPageNum(Param.getPageNum());

        // ????????????
        long total = hits.getTotalHits().value;

        result.setTotal((int) total);
        // ????????????????????????
        int totalPages = (int) (total / 3 + (total % 3 == 0 ? 0 : 1));
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            list.add(i);
        }
        result.setPageNavs(list);
        result.setTotalPages(totalPages);
        // 6.???????????????????????????
        if (Param.getAttrs() != null) {
            List<SearchResult.NavVo> navVos = Param.getAttrs().stream().map(attr -> {
                SearchResult.NavVo navVo = new SearchResult.NavVo();
                String[] s = attr.split("_");
                navVo.setNavValue(s[1]);
                R r = productFeignService.info(Long.parseLong(s[0]));
                // ??????????????????????????????????????? ????????????????????????
                result.getAttrIds().add(Long.parseLong(s[0]));
                String code = String.valueOf(r.get("code"));
                if ("0".equalsIgnoreCase(code)) {
                    Map<String, Object> data = (Map<String, Object>) r.get("attr");
                    if (data != null) {
                        String attrName = (String) data.get("attrName");
                        navVo.setName(attrName);
                    }
                } else {
                    // ???????????????id????????????
                    navVo.setName(s[0]);
                }
                // ???????????????????????? ??????????????????
                String replace = replaceQueryString(Param, attr, "attrs");
                navVo.setLink("http://search.storeTemplate.com/index.html?" + replace);
                return navVo;
            }).collect(Collectors.toList());
            result.setNavs(navVos);
        }

        // ??????
        if (Param.getBrandId() != null && Param.getBrandId().size() > 0) {
            List<SearchResult.NavVo> navs = result.getNavs();
            SearchResult.NavVo navVo = new SearchResult.NavVo();
            navVo.setName("??????:");
            // TODO ????????????????????????
            List<BrandTo> brandInfos = productFeignService.brandInfo(Param.getBrandId());
            StringBuffer buffer = new StringBuffer();
            // ??????????????????ID
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
     * ????????????
     * key ??????????????????key
     */
    private String replaceQueryString(SearchParam Param, String value, String key) {
        String encode = null;
        try {
            encode = URLEncoder.encode(value, "UTF-8");
            // ??????????????????????????????java????????????
            encode = encode.replace("+", "%20");
            encode = encode.replace("%28", "(").replace("%29", ")");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Param.get_queryString().replace("&" + key + "=" + encode, "");
    }


}