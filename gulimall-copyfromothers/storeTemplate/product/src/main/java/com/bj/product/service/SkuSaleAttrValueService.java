package com.bj.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bj.product.entity.SkuSaleAttrValueEntity;
import com.bj.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-02 21:08:16
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<String> getSkuSaleAttrValuesAsStringList(Long skuId);
}

