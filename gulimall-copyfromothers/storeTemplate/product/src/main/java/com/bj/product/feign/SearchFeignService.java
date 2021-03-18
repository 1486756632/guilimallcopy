package com.bj.product.feign;

import com.bj.dto.es.SkuEsModel;
import com.bj.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("storeTemplate.search")
public interface SearchFeignService {
    @PostMapping("/search/search/productUp/")
    public R productUp(@RequestBody List<SkuEsModel> skuEsModels);
}
