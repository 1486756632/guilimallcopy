package com.bj.cupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@MapperScan("com.bj.cupon.dao")
@SpringBootApplication
public class CuponApplication {

    public static void main(String[] args) {
        SpringApplication.run(CuponApplication.class, args);
    }

}
