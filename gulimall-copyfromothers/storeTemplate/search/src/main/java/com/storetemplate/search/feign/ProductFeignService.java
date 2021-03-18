package com.storetemplate.search.feign;

import com.bj.utils.R;
import com.bj.dto.BrandTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @ClassName ProductFeignService
 * @Description TODO
 * @Author 13011
 * @Date 2020/11/17 20:30
 * @Version 1.0
 **/
@FeignClient("storeTemplate.product")
public interface ProductFeignService {
    @GetMapping("product/attr/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId);
    @GetMapping("/product/brand/infos")
    @ResponseBody
    List<BrandTo> brandInfo(@RequestBody List<Long> brandIds);
}
