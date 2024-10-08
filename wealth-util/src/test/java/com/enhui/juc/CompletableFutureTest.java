package com.enhui.juc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CompletableFutureTest {
  static volatile Throwable producerFailure;

  public static void main(String[] args) throws InterruptedException {
    final AtomicInteger integer = new AtomicInteger();
    ExecutorService executorService =
        Executors.newSingleThreadExecutor(r -> new Thread(r, "test-" + integer.getAndIncrement()));
    ExecutorService pool = Executors.newSingleThreadExecutor();
    checkProducerFailure();
    CompletableFuture.runAsync(
            () -> {
              System.out.println("first--" + Thread.currentThread().getName());
              try {
                // sleep 执行耗时会影响异常处理的线程
                Thread.sleep(3000);
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
              //                            throw new RuntimeException("error");
            },
            executorService)
        .thenRun(
            () -> {
              System.out.println("second--" + Thread.currentThread().getName());
              try {
                // sleep 执行耗时会影响异常处理的线程
                Thread.sleep(3000);
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
              throw new RuntimeException("error");
            })
        .thenRun(
            () -> {
              System.out.println("third--" + Thread.currentThread().getName());
            })
        .exceptionally(
            e -> {
              System.out.println("exception1--" + Thread.currentThread().getName());
              executorService.shutdownNow();
              pool.shutdownNow();
              System.out.println("shut down--" + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    throw new RuntimeException(e1);
                }
              producerFailure = e;
              System.out.println("exception2--" + Thread.currentThread().getName());
              return null;
            });

    while (Thread.activeCount() > 2) {
      TimeUnit.MILLISECONDS.sleep(100);
    }
  }

  private static void checkProducerFailure() {
    new Thread(
            () -> {
              while (true) {
                if (producerFailure != null) {
                  throw new RuntimeException(
                      Thread.currentThread().getName() + ", This task will be stopped.",
                      producerFailure);
                }
                try {
                  TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                  throw new RuntimeException(e);
                }
              }
            },
            "checkErrorThread")
        .start();
  }
}
