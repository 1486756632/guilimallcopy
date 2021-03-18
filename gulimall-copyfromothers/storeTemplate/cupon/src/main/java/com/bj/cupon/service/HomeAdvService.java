package com.bj.cupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bj.utils.PageUtils;
import com.bj.cupon.entity.HomeAdvEntity;

import java.util.Map;

/**
 * 首页轮播广告
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-08 23:53:13
 */
public interface HomeAdvService extends IService<HomeAdvEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

