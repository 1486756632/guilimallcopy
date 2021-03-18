package com.bj.product.dao;

import com.bj.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bj.product.entity.AttrGroupEntity;
import com.bj.product.vo.SpuItemAttrGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-02 22:32:15
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

	public void deleteBatch(@Param("entityList") List<AttrAttrgroupRelationEntity> entityList);

    List<AttrGroupEntity> getAttrBycatalogId(@Param("catalogId") Long catalogId);

    List<SpuItemAttrGroup> getAttrBySpuIdAndCatId(@Param("spuId") Long spuId, @Param("catalogId") Long catalogId);
}
