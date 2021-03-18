package com.bj.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bj.order.entity.OrderReturnReasonEntity;
import com.bj.utils.PageUtils;

import java.util.Map;

/**
 * 退货原因
 *
 * @author bj
 * @email sunlightcs@gmail.com
 * @date 2020-08-08 23:14:31
 */
public interface OrderReturnReasonService extends IService<OrderReturnReasonEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

