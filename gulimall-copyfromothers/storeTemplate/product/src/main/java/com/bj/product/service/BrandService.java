package com.bj.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bj.product.entity.BrandEntity;
import com.bj.utils.PageUtils;

import java.util.Map;

/**
 * 品牌
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-02 22:32:15
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateDetail(BrandEntity brand);
}

