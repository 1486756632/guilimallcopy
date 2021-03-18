package com.bj.inventory.vo;

import lombok.Data;

/**
 * @ClassName PurchaseItemVo
 * @Description TODO
 * @Author 13011
 * @Date 2020/10/7 10:27
 * @Version 1.0
 **/
@Data
public class PurchaseItemVo {
    private Long itemId;
    private Integer status;
    private String reason;
}
