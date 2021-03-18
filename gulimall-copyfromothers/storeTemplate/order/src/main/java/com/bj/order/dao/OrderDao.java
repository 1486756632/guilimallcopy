package com.bj.order.dao;

import com.bj.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单
 * 
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-08 23:14:31
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
    void updateOrderStatus(@Param("orderSn") String orderSn, @Param("code") Integer code);
}
