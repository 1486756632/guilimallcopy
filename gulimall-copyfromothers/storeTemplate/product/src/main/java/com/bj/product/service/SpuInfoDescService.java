package com.bj.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bj.product.entity.SpuInfoDescEntity;
import com.bj.utils.PageUtils;

import java.util.Map;

/**
 * spu信息介绍
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-02 21:08:16
 */
public interface SpuInfoDescService extends IService<SpuInfoDescEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

