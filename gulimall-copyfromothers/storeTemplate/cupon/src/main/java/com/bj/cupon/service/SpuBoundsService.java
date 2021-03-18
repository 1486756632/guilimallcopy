package com.bj.cupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bj.utils.PageUtils;
import com.bj.cupon.entity.SpuBoundsEntity;

import java.util.Map;

/**
 * 商品spu积分设置
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-08 23:53:13
 */
public interface SpuBoundsService extends IService<SpuBoundsEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

