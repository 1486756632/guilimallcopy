package com.bj.product.dao;

import com.bj.product.entity.BrandEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 品牌
 * 
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-02 22:32:15
 */
@Mapper
public interface BrandDao extends BaseMapper<BrandEntity> {
    @Select(value = "select * from pms_brand")
    public List<BrandEntity> test();
}
