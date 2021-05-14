package com.atguigu.gulimall.product.feign;

import com.atguigu.common.to.es.SkuEsModel;
import com.atguigu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

//@FeignClient("storeTemplate.search")
public interface SearchFeignService {
    @PostMapping("/search/search/productUp/")
    public R productUp(@RequestBody List<SkuEsModel> skuEsModels);
}
