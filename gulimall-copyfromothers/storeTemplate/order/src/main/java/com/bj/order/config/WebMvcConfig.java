package com.bj.order.config;

import com.bj.order.interceptor.LoginUserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
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
    @Autowired
    private LoginUserInterceptor loginUserInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginUserInterceptor).addPathPatterns("/**");
    }

    //公共视图映射
    @Override
   public void addViewControllers(ViewControllerRegistry registry){
       registry.addViewController("/order.html").setViewName("order");
       registry.addViewController("/proCountPrice.html").setViewName("reg");
       registry.addViewController("/pay.html").setViewName("pay");
       registry.addViewController("/paing.html").setViewName("paing");

   }
}
