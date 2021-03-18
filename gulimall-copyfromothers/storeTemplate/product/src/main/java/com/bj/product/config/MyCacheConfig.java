package com.bj.product.config;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @ClassName MyCacheConfig
 * @Description TODO
 * @Author 13011
 * @Date 2020/11/11 20:11
 * @Version 1.0
 **/
@EnableConfigurationProperties(CacheProperties.class)
@Configuration
@EnableCaching
public class MyCacheConfig {
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties){
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
        redisCacheConfiguration=redisCacheConfiguration.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer( new GenericJackson2JsonRedisSerializer()));
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        /*
        * 定义此配置后CacheProperties不会被装备进容器，读取配置不生效，在自定义配置中手动加入容器读取配置
        * */
        if(redisProperties.getTimeToLive()!=null){
            redisCacheConfiguration=redisCacheConfiguration.entryTtl(redisProperties.getTimeToLive());
        }
        if(redisProperties.getKeyPrefix()!=null){
            redisCacheConfiguration=redisCacheConfiguration.prefixKeysWith(redisProperties.getKeyPrefix());
        }
        if(!redisProperties.isCacheNullValues()){
            redisCacheConfiguration.disableCachingNullValues();
        }
        if(!redisProperties.isUseKeyPrefix()){
         redisCacheConfiguration.disableKeyPrefix();
        }
        return redisCacheConfiguration;
    }

}
