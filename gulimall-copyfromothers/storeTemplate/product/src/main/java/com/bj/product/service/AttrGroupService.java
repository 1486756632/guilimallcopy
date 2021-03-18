package com.bj.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bj.product.entity.AttrAttrgroupRelationEntity;
import com.bj.product.entity.AttrGroupEntity;
import com.bj.product.vo.SpuItemAttrGroup;
import com.bj.utils.PageUtils;
import com.bj.utils.R;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-02 22:32:15
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, String catId);

    R relation(Long attrGroupId);

    R removeRelation(List<AttrAttrgroupRelationEntity> entities);

    R getNoRelation(Long attrGroupId,Map<String, Object> params);


    List<SpuItemAttrGroup> getAttrBySpuIdAndCatId(Long spuId, Long catalogId);
}

