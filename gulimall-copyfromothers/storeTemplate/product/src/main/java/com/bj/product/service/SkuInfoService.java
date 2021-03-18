package com.bj.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bj.product.entity.SkuInfoEntity;
import com.bj.product.vo.ItemSaleAttrVo;
import com.bj.product.vo.SkuItemVo;
import com.bj.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * sku信息
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-02 21:08:16
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageByContion(Map<String, Object> params);

    List<SkuInfoEntity> getSkuBySpuId(Long spuId);

    SkuItemVo getItem(Long skuId);

}

