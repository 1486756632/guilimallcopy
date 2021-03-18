package com.bj.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName OrderRequesterConfig
 * @Description TODO
 * @Author 13011
 * @Date 2021/1/24 15:18
 * @Version 1.0
 **/
@Configuration
public class OrderFeignConfig {
    @Bean("requestInterceptor")
    public RequestInterceptor requestInterceptor(){
        // Feign在远程调用之前都会先经过这个方法
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // RequestContextHolder拿到刚进来这个请求
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if(attributes != null){
                    HttpServletRequest request = attributes.getRequest();
                    if(request != null){
                        // 同步请求头数据
                        String cookie = request.getHeader("Cookie");
                        // 给新请求同步Cookie
                        template.header("Cookie", cookie);
                    }
                }
            }
        };
    }
}
