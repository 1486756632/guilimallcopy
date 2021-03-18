package com.bj.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @ClassName OrderApplication
 * @Description TODO
 * @Author 13011
 * @Date 2020/8/8 23:32
 * @Version 1.0
 **/
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableRabbit
@EnableFeignClients(basePackages = "com.bj.order.feign")
@EnableDiscoveryClient
@MapperScan("com.bj.order.dao")
@SpringBootApplication
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
