package com.atguigu.gulimall.product.dao;

import com.atguigu.gulimall.product.entity.SkuInfoEntity;
import com.atguigu.gulimall.product.vo.ItemSaleAttrVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * sku信息
 * 
 * @author leifengyang
 * @email leifengyang@gmail.com
 * @date 2019-10-01 21:08:49
 */
@Mapper
public interface SkuInfoDao extends BaseMapper<SkuInfoEntity> {
    List<ItemSaleAttrVo> getSaleAttrVosBySpuId(Long spuId);
}
