package com.bj.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName ThreadPoolConfigProperties
 * @Description TODO
 * @Author 13011
 * @Date 2020/11/21 19:43
 * @Version 1.0
 **/
@ConfigurationProperties(prefix = "store.thread")
@Component
@Data
public class ThreadPoolConfigProperties {
   private int corePoolSize;
   private int maximumPoolSize;
   private long keepAliveTime;
}
