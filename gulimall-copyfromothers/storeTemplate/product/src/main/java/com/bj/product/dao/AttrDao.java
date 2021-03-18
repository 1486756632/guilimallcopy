package com.bj.product.dao;

import com.bj.product.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bj.product.vo.AttrRspVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品属性
 * 
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-02 22:32:15
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {
    List<AttrRspVo> getDatas(Map<String,Object> map);
    Integer getDatsTotal(Map<String,Object> map);
}
