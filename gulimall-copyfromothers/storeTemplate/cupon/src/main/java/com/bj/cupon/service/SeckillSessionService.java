package com.bj.cupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bj.utils.PageUtils;
import com.bj.cupon.entity.SeckillSessionEntity;

import java.util.Map;

/**
 * 秒杀活动场次
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-08 23:53:13
 */
public interface SeckillSessionService extends IService<SeckillSessionEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

