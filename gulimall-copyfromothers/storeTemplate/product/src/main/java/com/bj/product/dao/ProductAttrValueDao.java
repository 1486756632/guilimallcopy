package com.bj.product.dao;

import com.bj.product.entity.ProductAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * spu属性值
 * 
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-02 21:08:16
 */
@Mapper
public interface ProductAttrValueDao extends BaseMapper<ProductAttrValueEntity> {

    void updateBatchBySpuId(@Param("spuId") Long spuId, @Param("attrValueEntities") List<ProductAttrValueEntity> attrValueEntities);
}
