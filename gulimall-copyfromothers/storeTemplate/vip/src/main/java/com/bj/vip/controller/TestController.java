package com.bj.vip.controller;

import com.bj.utils.R;
import com.bj.vip.feign.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestController
 * @Description TODO
 * @Author 13011
 * @Date 2020/8/9 11:43
 * @Version 1.0
 **/
@RestController
@RequestMapping("vip/growthchangehistory")
public class TestController {
    @Autowired
    private CouponService couponService;
    @RequestMapping("testFeign")
    public R testFeign() {
        R feign = couponService.feign();
        return feign;
    }

}
