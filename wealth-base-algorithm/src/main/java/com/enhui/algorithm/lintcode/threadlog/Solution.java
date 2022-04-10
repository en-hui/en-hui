package com.enhui.algorithm.lintcode.threadlog;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Solution {

  //  public static void createLog() throws Exception {
  //    CountDownLatch countDownLatch = new CountDownLatch(16);
  //    // modify the following code
  //    for (int i = 1; i < 17; i++) {
  //      final int temp = i;
  //      Thread thread =
  //          new Thread(
  //              () -> {
  //                Main.parseLog(temp);
  //                countDownLatch.countDown();
  //              });
  //      thread.start();
  //    }
  //    countDownLatch.await();
  //  }

  public static void createLog() throws Exception {
    AtomicInteger count = new AtomicInteger();
    ThreadPoolExecutor pool =
        new ThreadPoolExecutor(
            8,
            8,
            100,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(8),
            r -> new Thread(r, "PrintLog-" + count.incrementAndGet()),
            new ThreadPoolExecutor.AbortPolicy());
    CountDownLatch countDownLatch = new CountDownLatch(16);
    try {
      for (int i = 1; i < 17; i++) {
        int finalI = i;
        pool.execute(
            () -> {
              Main.parseLog(finalI);
              countDownLatch.countDown();
            });
      }
      countDownLatch.await();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      pool.shutdown();
    }
  }
}
