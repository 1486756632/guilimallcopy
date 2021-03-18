package com.bj.inventory.vo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName MergeVo
 * @Description TODO
 * @Author 13011
 * @Date 2020/10/6 15:35
 * @Version 1.0
 **/
@Data
public class MergeVo {
    private Long purchaseId;//采购单id
    private List<Long> items;//要合并的采购需求id
}
