package com.bj.product.dao;

import com.bj.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-02 22:32:15
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
