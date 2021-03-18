package com.storetemplate.search.controller;

import com.atguigu.common.to.es.SkuEsModel;
import com.atguigu.common.utils.R;
import com.storetemplate.search.service.ProductService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName EsSaveController
 * @Description TODO
 * @Author 13011
 * @Date 2020/11/4 11:22
 * @Version 1.0
 **/
@RestController
@RequestMapping("/search/search")
public class EsSaveController {
    @Resource
    private ProductService productService;

    @RequestMapping("/productUp")
     public R productUp(@RequestBody List<SkuEsModel> skuEsModels){
        Boolean b = productService.productUp(skuEsModels);
        if(b){
            return R.ok();
        }
        return R.error();
     };

}
