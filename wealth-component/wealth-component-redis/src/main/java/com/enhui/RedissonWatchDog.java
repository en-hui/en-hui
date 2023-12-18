package com.enhui;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedissonWatchDog {
  private static RedissonClient redissonClient;

  public static void initRedisson() {
    // 配置 Redisson 客户端
    Config config = new Config();
    config
        .useSingleServer()
        .setAddress("redis://dp-redis:6379")
        .setPassword("Datapipeline123"); // Redis 服务器地址

    // 创建 Redisson 客户端
    redissonClient = Redisson.create(config);
    System.out.println("创建 Redisson 客户端");
  }

  public static void killRedisson() {
    // 关闭 Redisson 客户端
    redissonClient.shutdown();
    System.out.println("关闭 Redisson 客户端");
  }

  public static void lockBusiness(CountDownLatch countDownLatch) {
    // 获取锁对象
    RLock lock = redissonClient.getLock("test");

    try {
      // leaseTime 只有小于0，才会启动看门狗进行续约（不同版本判断不同）
      boolean isLocked = lock.tryLock(10, 3, java.util.concurrent.TimeUnit.SECONDS);

      if (isLocked) {
        // 锁获取成功，执行需要锁保护的操作
        System.out.println("加锁成功");
        // 执行业务逻辑
        Thread.sleep(7 * 1000);
      } else {
        System.out.println("加锁失败");
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      // 释放锁
//      lock.unlock();
      countDownLatch.countDown();
      System.out.println("业务执行完毕，理论上应该此时释放锁");
    }
  }

  public static void main(String[] args) throws InterruptedException {
    initRedisson();

    CountDownLatch countDownLatch = new CountDownLatch(2);
    ExecutorService service = Executors.newFixedThreadPool(2);
    for (int i = 0; i < 2; i++) {
      service.submit(
          () -> {
            lockBusiness(countDownLatch);
          });
    }

    countDownLatch.await();
    killRedisson();
    System.exit(0);
  }
}
