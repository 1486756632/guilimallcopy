package com.bj.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName SpuBoundSTo
 * @Description TODO
 * @Author 13011
 * @Date 2020/9/21 19:23
 * @Version 1.0
 **/
@Data
public class SpuBoundsTo {

    private Long spuId;
    /**
     * 成长积分
     */
    private BigDecimal growBounds;
    /**
     * 购物积分
     */
    private BigDecimal buyBounds;
}
