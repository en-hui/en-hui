package com.enhui;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class WealthRedissonClient {
  private static RedissonClient redissonClient;

  public synchronized RedissonClient getRedissonClient() {
    if (redissonClient == null) {
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
    return redissonClient;
  }

  public synchronized void close() {
    // 关闭 Redisson 客户端
    redissonClient.shutdown();
    redissonClient = null;
    System.out.println("关闭 Redisson 客户端");
  }
}
