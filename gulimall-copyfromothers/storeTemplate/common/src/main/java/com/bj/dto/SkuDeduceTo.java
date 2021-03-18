package com.bj.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName SkuDeduceTo
 * @Description TODO
 * @Author 13011
 * @Date 2020/9/21 19:44
 * @Version 1.0
 **/
@Data
public class SkuDeduceTo {
    private Long skuId;
    private Integer fullCount;
    private BigDecimal discount;
    private Integer countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private Integer priceStatus;
    private List<MemberPrice> memberPrice;
}
