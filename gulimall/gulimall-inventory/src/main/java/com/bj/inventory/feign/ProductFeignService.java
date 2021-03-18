package com.bj.inventory.feign;

import com.bj.dto.SkuDeduceTo;
import com.bj.dto.SpuBoundsTo;
import com.bj.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @ClassName CouponFeignService
 * @Description TODO
 * @Author 13011
 * @Date 2020/9/21 19:17
 * @Version 1.0
 **/
@FeignClient("storeTemplate.product")
public interface ProductFeignService {
    @GetMapping("product/skuinfo/info/{skuId}")
    public R skuInfo(@PathVariable("skuId") Long skuId);

}
