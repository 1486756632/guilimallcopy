package com.storeTemplate.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ProductThreadConfig
 * @Description TODO
 * @Author 13011
 * @Date 2020/11/21 19:40
 * @Version 1.0
 **/
@Configuration
public class CartThreadConfig {
    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigProperties threadPoolConfigProperties) {
       return new ThreadPoolExecutor(threadPoolConfigProperties.getCorePoolSize()
                , threadPoolConfigProperties.getMaximumPoolSize()
                ,threadPoolConfigProperties.getKeepAliveTime()
                , TimeUnit.SECONDS
                ,new LinkedBlockingDeque<>(20000)
                ,Executors.defaultThreadFactory()
                ,new ThreadPoolExecutor.AbortPolicy()
        );
    }

}
