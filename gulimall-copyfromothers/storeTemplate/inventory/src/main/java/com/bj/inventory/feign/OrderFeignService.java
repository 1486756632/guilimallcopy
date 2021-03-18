package com.bj.inventory.feign;

import com.bj.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <p>Title: OrderFeignService</p>
 * Description：
 * date：2020/7/3 22:15
 */
@FeignClient("storeTemplate.order")
public interface OrderFeignService {

	/**
	 * 查询订单状态
	 */
	@GetMapping("/order/order/status/{orderSn}")
	R getOrderStatus(@PathVariable("orderSn") String orderSn);
}
