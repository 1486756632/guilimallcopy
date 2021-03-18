package com.bj.cupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bj.utils.PageUtils;
import com.bj.cupon.entity.SeckillSkuRelationEntity;

import java.util.Map;

/**
 * 秒杀活动商品关联
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-08 23:53:13
 */
public interface SeckillSkuRelationService extends IService<SeckillSkuRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

