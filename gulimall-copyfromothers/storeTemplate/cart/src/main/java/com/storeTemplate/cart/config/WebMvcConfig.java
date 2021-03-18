package com.storeTemplate.cart.config;

import com.storeTemplate.cart.intercepter.CartInterCepter;
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
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CartInterCepter()).addPathPatterns("/**");
    }

}
