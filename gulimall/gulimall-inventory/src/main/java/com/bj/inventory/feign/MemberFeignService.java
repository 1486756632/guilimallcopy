package com.bj.inventory.feign;

import com.bj.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>Title: MemberFeignService</p>
 * Description：
 * date：2020/7/1 12:56
 */
@FeignClient("storeTemplate.vip")
public interface MemberFeignService {

	@RequestMapping("/member/memberreceiveaddress/info/{id}")
	R addrInfo(@PathVariable("id") Long id);
}
