package com.bj.inventory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bj.inventory.entity.PurchaseDetailEntity;
import com.bj.inventory.vo.WareSkuLockVo;
import com.bj.utils.PageUtils;
import com.bj.inventory.entity.WareSkuEntity;
import com.bj.utils.R;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-08 23:40:11
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageByContidion(Map<String, Object> params);

    void addStock(List<PurchaseDetailEntity> detailEntities);

    R hasStockBySku(List<Long> skuIds);

    Boolean orderLockStock(WareSkuLockVo vo);
}

