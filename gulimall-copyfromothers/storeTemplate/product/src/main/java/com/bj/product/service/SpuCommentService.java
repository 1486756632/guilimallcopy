package com.bj.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bj.product.entity.SpuCommentEntity;
import com.bj.utils.PageUtils;

import java.util.Map;

/**
 * 商品评价
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-02 21:08:16
 */
public interface SpuCommentService extends IService<SpuCommentEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

