package com.atguigu.gulimall.product.feign;

import com.atguigu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @ClassName WareFeignService
 * @Description TODO
 * @Author 13011
 * @Date 2020/11/3 16:48
 * @Version 1.0
 **/
@FeignClient("storeTemplate.inventory")
public interface WareFeignService {
    @PostMapping("ware/waresku/hasStock")
    public R hasStockBySku(@RequestBody List<Long> skuIds);
}
