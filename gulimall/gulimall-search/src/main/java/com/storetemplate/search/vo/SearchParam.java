package com.storetemplate.search.vo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName SearchParam
 * @Description TODO
 * @Author 13011
 * @Date 2020/11/12 20:10
 * @Version 1.0
 **/
@Data
public class SearchParam {
  private String keyword;//页面全文检索关键字
  private Long catalog3Id;//三级分类id
  private String sort;//排序条件
  private Integer hasStock;//是否只显示有货
  private String skuPrice;//价格区间查询
  private List<Long>brandId;//按照所选的一个或者多个品牌进行查询
  private List<String> attrs;//按照属性进行查询
  private  Integer pageNum=1;
  /**
   * 原生所有查询属性
   */
  private String _queryString;
}
