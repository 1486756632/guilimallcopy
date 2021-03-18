package com.bj.inventory.dao;

import com.bj.inventory.entity.PurchaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购信息
 * 
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-08 23:40:11
 */
@Mapper
public interface PurchaseDao extends BaseMapper<PurchaseEntity> {
	
}
