package com.bj.product.feign;

import com.bj.dto.SkuDeduceTo;
import com.bj.dto.SpuBoundsTo;
import com.bj.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @ClassName CouponFeignService
 * @Description TODO
 * @Author 13011
 * @Date 2020/9/21 19:17
 * @Version 1.0
 **/
@FeignClient("storeTemplate.coupon")
public interface CouponFeignService {
    @PostMapping("cupon/spubounds/save")
    public R save(@RequestBody SpuBoundsTo spuBounds);

    @PostMapping("cupon/skufullreduction/saveSkuReduce")
    public R saveSkuReduce(@RequestBody SkuDeduceTo skuDeduceTo);
}
