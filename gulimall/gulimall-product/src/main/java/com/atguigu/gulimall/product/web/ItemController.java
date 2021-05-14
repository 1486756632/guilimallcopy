package com.bj.product.web;

import com.bj.product.entity.CategoryEntity;
import com.bj.product.service.SkuInfoService;
import com.bj.product.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName ItemController
 * @Description TODO
 * @Author 13011
 * @Date 2020/11/19 11:11
 * @Version 1.0
 **/
@Controller
public class ItemController {
    @Autowired
    SkuInfoService skuInfoService;
    @RequestMapping ("{skuId}.html")
    public String index(@PathVariable("skuId") Long skuId, Model model) {
        SkuItemVo skuItemVo=skuInfoService.getItem( skuId);
        model.addAttribute("item",skuItemVo);
        return "item";
    }
}
