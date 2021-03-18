package com.bj.cupon.dao;

import com.bj.cupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-08 23:53:13
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
