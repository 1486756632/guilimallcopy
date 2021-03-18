package com.bj.inventory.vo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName PurchaseFinshedVo
 * @Description TODO
 * @Author 13011
 * @Date 2020/10/7 10:31
 * @Version 1.0
 **/
@Data
public class PurchaseFinshedVo {
    private Long purchaseId;//采购单id
    private List<PurchaseItemVo> items;
}
