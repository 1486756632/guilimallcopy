package com.bj.product.controller;

import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TestRController
 * @Description TODO
 * @Author 13011
 * @Date 2020/11/10 16:33
 * @Version 1.0
 **/
@RestController
public class TestRController {
    @Autowired
    private RedissonClient redissonClient;
    @GetMapping("/hello")
    public String hello() {
        RLock rlock = redissonClient.getLock("rlock");
        rlock.lock();
        System.out.println("加了分布式锁"+Thread.currentThread().getName());
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            //redission默认30s后自动释放锁，无须担心系统宕机造成死锁
            //中间业务执行过长redission看门狗会自动续期，续期时间默认30s
            rlock.unlock();
            System.out.println("分布式锁解锁"+Thread.currentThread().getName());
        }
        return "hello";
    }

    @GetMapping("/write")
    public String write() {
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("wr-lock");
        RLock rLock = readWriteLock.writeLock();
        rLock.lock();
        System.out.println("加了分布式锁"+Thread.currentThread().getName());
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            //redission默认30s后自动释放锁，无须担心系统宕机造成死锁
            //中间业务执行过长redission看门狗会自动续期，续期时间默认30s
            rLock.unlock();
            System.out.println("分布式锁解锁"+Thread.currentThread().getName());
        }
        return "write";
    }
    @GetMapping("/read")
    public String read() {
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("wr-lock");
        RLock rLock = readWriteLock.readLock();

        rLock.lock();
        System.out.println("加了分布式锁"+Thread.currentThread().getName());
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            //redission默认30s后自动释放锁，无须担心系统宕机造成死锁
            //中间业务执行过长redission看门狗会自动续期，续期时间默认30s
            rLock.unlock();
            System.out.println("分布式锁解锁"+Thread.currentThread().getName());
        }
        return "read";
    }
    @GetMapping("/park")
    public String park() throws InterruptedException {
        RSemaphore sem = redissonClient.getSemaphore("sem");
        sem.acquire();
        System.out.println("加了信号量"+Thread.currentThread().getName());
        return "park";
    }
    @GetMapping("/go")
    public String go() throws InterruptedException {
        RSemaphore sem = redissonClient.getSemaphore("sem");
        sem.release();
        System.out.println("释放信号量"+Thread.currentThread().getName());
        return "go";
    }
}
