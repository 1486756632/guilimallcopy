package com.storetemplate.search.vo;

import com.atguigu.common.to.es.SkuEsModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName SearchResult
 * @Description TODO
 * @Author 13011
 * @Date 2020/11/12 21:17
 * @Version 1.0
 **/
@Data
public class SearchResult {
    //所有商品信息
private List<SkuEsModel> products;
private Integer pageNum;
private Integer total;
private Integer totalPages;
private List<Integer> pageNavs;
private List<BrandVo> brands;//所有涉及的查询品牌结果
private List<CatalogVo> catalogVos;//所有涉及的查询分类结果
private List<AttrVo> attrVos;//所有涉及的查询属性结果
    //	================以上是返回给页面的所有信息================

    // 面包屑导航数据
    private  List<NavVo> navs = new ArrayList<>();

    /**
     * 便于判断当前id是否被使用
     */
    private List<Long> attrIds = new ArrayList<>();
    @Data
    public static class NavVo{
        private String name;

        private String navValue;

        private String link;
    }
    @Data
    public static class BrandVo{

        private Long brandId;

        private String brandName;

        private String brandImg;
    }

    @Data
    public static class CatalogVo{

        private Long catalogId;

        private String catalogName;
    }

    @Data
    public static class AttrVo{

        private Long attrId;

        private String attrName;

        private List<String> attrValue;
    }
}
