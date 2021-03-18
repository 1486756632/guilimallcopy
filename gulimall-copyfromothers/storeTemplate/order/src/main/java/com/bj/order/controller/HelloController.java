package com.bj.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName HelloController
 * @Description TODO
 * @Author 13011
 * @Date 2021/1/23 22:50
 * @Version 1.0
 **/
@Controller
public class HelloController {
    @RequestMapping("{page}.html")
    public String page(@PathVariable("page") String page){
        return page;
    }
}
