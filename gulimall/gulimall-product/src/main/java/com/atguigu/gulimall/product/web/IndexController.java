package com.atguigu.gulimall.product.web;

import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.gulimall.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


/**
 * @ClassName IndexController
 * @Description TODO
 * @Author 13011
 * @Date 2020/11/6 10:25
 * @Version 1.0
 **/
@Controller
public class IndexController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping({"/", "/index.html"})
    public String index(Model model) {
        List<CategoryEntity> categoryEntityList = categoryService.selectFirstCategory();
        model.addAttribute("categorys", categoryEntityList);
        return "index";
    }

    @GetMapping("/index/json/catalog.json")
    @ResponseBody
    public Map<String, List<Catelog2Vo>> getCatlog() {
        return categoryService.getCatlog();
    }


}
