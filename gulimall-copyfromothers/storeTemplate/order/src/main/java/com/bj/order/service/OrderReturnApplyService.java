package com.bj.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bj.order.entity.OrderReturnApplyEntity;
import com.bj.utils.PageUtils;

import java.util.Map;

/**
 * 订单退货申请
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-08 23:14:31
 */
public interface OrderReturnApplyService extends IService<OrderReturnApplyEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

