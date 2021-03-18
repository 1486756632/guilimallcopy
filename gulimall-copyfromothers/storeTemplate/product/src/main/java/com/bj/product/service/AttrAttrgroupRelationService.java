package com.bj.product.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.bj.product.entity.AttrAttrgroupRelationEntity;
import com.bj.utils.PageUtils;
import com.bj.utils.R;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-02 22:32:15
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    R addRelation(List<AttrAttrgroupRelationEntity> entities);

    R showBaseAttrsByCatId(Long catalogId);
}

