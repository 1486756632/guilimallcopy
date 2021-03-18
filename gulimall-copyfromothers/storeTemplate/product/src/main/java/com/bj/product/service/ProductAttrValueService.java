package com.bj.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bj.product.entity.ProductAttrValueEntity;
import com.bj.utils.PageUtils;
import com.bj.utils.R;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-02 21:08:16
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    R updateForSpu(Long spuId, List<ProductAttrValueEntity> attrValueEntities);
}

