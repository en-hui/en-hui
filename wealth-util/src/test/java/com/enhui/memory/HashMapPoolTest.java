package com.enhui.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.GraphLayout;

/** 对象池尝试 */
public class HashMapPoolTest {

  @Test
  public void testMapPool() throws Exception {
    List<HashMap<String, Object>> list = new ArrayList<>();
    while (true) {
      for (int i = 0; i < 10; i++) {
        HashMap<String, Object> map = HashMapPool.borrowMap();
        System.out.println("map 赋值前总内存占用：" + GraphLayout.parseInstance(map).totalSize());
        JolResearch.putMap(map);
        System.out.println("map 赋值后总内存占用：" + GraphLayout.parseInstance(map).totalSize());
        list.add(map);
      }

      for (HashMap<String, Object> map : list) {
        HashMapPool.returnMap(map);
      }
      list.clear();
      System.out.println("处理完毕一轮");
      TimeUnit.SECONDS.sleep(3L);
    }
  }

  /**
   * 定义HashMap工厂类
   *
   * <p>继承BasePooledObjectFactory，实现HashMap的创建、销毁和状态重置逻辑
   */
  private static class HashMapFactory extends BasePooledObjectFactory<HashMap<String, Object>> {

    @Override
    public HashMap<String, Object> create() throws Exception {
      return new HashMap<>(); // 创建新HashMap
    }

    @Override
    public PooledObject<HashMap<String, Object>> wrap(HashMap<String, Object> map) {
      return new DefaultPooledObject<>(map);
    }

    @Override
    public void passivateObject(PooledObject<HashMap<String, Object>> p) {
      p.getObject().clear(); // 归还前清空数据，避免旧数据残留[1,7](@ref)
    }

    @Override
    public boolean validateObject(PooledObject<HashMap<String, Object>> p) {
      return true; // 可扩展：校验HashMap状态（如内存泄漏检测）
    }
  }

  /**
   * 配置对象池参数
   *
   * <p>通过GenericObjectPoolConfig设置池行为
   */
  private static class HashMapPool {
    private static GenericObjectPool<HashMap<String, Object>> pool;

    static {
      GenericObjectPoolConfig<HashMap<String, Object>> config = new GenericObjectPoolConfig<>();
      config.setMaxTotal(50000); // 最大对象数
      config.setMinIdle(1000); // 最小空闲对象数（避免频繁扩容）
      config.setMaxIdle(50000); // 最大空闲对象数（防止内存浪费）
      config.setTestOnBorrow(true); // 借用时验证对象有效性[4,7](@ref)
      pool = new GenericObjectPool<>(new HashMapFactory(), config);
    }

    public static HashMap<String, Object> borrowMap() throws Exception {
      return pool.borrowObject();
    }

    public static void returnMap(HashMap<String, Object> map) {
      pool.returnObject(map);
    }
  }
}
