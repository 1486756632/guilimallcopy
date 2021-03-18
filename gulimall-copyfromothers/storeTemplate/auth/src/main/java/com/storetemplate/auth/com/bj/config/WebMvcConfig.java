package com.storetemplate.auth.com.bj.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName WebMvcConfig
 * @Description TODO
 * @Author 13011
 * @Date 2020/12/27 16:14
 * @Version 1.0
 **/
@Configuration
public class WebMvcConfig  implements WebMvcConfigurer {
    //公共视图映射
    @Override
   public void addViewControllers(ViewControllerRegistry registry){
       registry.addViewController("/index.html").setViewName("index");
       registry.addViewController("/reg.html").setViewName("reg");
   }
}
