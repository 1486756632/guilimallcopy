package com.storetemplate.auth.com.bj.feign;

import com.atguigu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName OutgatewayFeignClint
 * @Description TODO
 * @Author 13011
 * @Date 2021/1/1 19:12
 * @Version 1.0
 **/
@FeignClient("storeTemplate.outGateway")
public interface OutgatewayFeignClint {
    @PostMapping("/outGateway/sms/sendSms")
    public R sendSms(@RequestParam("phone") String phone, @RequestParam("code") String code) ;
}
