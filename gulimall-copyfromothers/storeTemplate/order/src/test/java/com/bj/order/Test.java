package com.bj.order;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Test
 * @Description TODO
 * @Author 13011
 * @Date 2021/2/21 22:54
 * @Version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class Test {
    @Autowired
    RedisTemplate redisTemplate;

    @org.junit.Test
    public void test5() throws Exception {
        List<String> keys = new ArrayList<String>();
        keys.add("test_key1");
        List<String> args = new ArrayList<String>();
        args.add("hello,key1");
        String LUA = "redis.call('del', KEYS[1]); return ARGV[1]";
        //spring自带的执行脚本方法中，集群模式直接抛出不支持执行脚本异常，此处拿到原redis的connection执行脚本
        String result = (String)redisTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                Object nativeConnection = connection.getNativeConnection();
                // 集群模式和单点模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                // 集群
                if (nativeConnection instanceof JedisCluster) {
                    System.out.println("---------"+"走了集群");
                    return (String) ((JedisCluster) nativeConnection).eval(LUA, keys, args);
                }

                // 单点
                else if (nativeConnection instanceof Jedis) {
                    return (String) ((Jedis) nativeConnection).eval(LUA, keys, args);
                }
                return null;
            }
        });
        System.out.println(result);
    }
}
