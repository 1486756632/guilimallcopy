package com.bj.vip.feign;

import com.atguigu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName CouponController
 * @Description TODO
 * @Author 13011
 * @Date 2020/8/9 11:35
 * @Version 1.0
 **/
@FeignClient("storeTemplate.coupon")
public interface CouponService {
   @RequestMapping("cupon/feign")
   public R feign();


}
