package com.bj.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bj.product.entity.SpuImagesEntity;
import com.bj.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * spu图片
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-02 21:08:16
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveImgs(Long id, List<String> images);
}

