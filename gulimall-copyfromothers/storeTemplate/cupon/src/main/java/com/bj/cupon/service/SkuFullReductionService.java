package com.bj.cupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bj.dto.SkuDeduceTo;
import com.bj.utils.PageUtils;
import com.bj.cupon.entity.SkuFullReductionEntity;
import com.bj.utils.R;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-08 23:53:13
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    R saveSkuReduce(SkuDeduceTo skuDeduceTo);
}

