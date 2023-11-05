package com.enhui.zookeeper.lock;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class LockMain {

  @Test
  public void testLock() throws Exception {
    for (int i = 0; i < 10; i++) {
      new Thread(
              () -> {
                ZkLock zkLock = new ZkLock(LockType.WORK_LOCK);
                // 每个线程抢锁
                try {
                  zkLock.tryLock();
                } catch (Exception e) {
                  log.error("zkStudy====尝试加锁失败", e);
                }
                // 执行业务逻辑
                log.info("zkStudy====『{}』 线程抢锁成功，执行业务逻辑", Thread.currentThread().getName());
                try {
                  Thread.sleep(1000);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
                // 释放锁
                try {
                  zkLock.unLock();
                } catch (Exception e) {
                  log.error("zkStudy====尝试释放锁失败", e);
                }
              })
          .start();
    }
    while (Thread.activeCount() > 2) {
      Thread.sleep(5000);
      System.out.println("主线程等待，剩余线程数量：" + Thread.activeCount());
    }
  }
}
