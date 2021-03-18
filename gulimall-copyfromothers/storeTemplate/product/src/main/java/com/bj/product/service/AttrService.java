package com.bj.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bj.product.entity.AttrEntity;
import com.bj.product.vo.AttrVo;
import com.bj.utils.PageUtils;
import com.bj.utils.R;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-02 22:32:15
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params,Long catId);

    R saveDetail(AttrVo attr);

    PageUtils getDatas(Map<String, Object> params, Long catId, String type);

    R updateAttrById(AttrVo attr);

    R listForSpu(Long spuId);

    List<Long> getSearchAttr(List<Long> attrIds);
}

