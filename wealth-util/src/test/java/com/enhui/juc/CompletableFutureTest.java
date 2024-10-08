package com.enhui.juc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CompletableFutureTest {
  static volatile Throwable producerFailure;

  public static void main(String[] args) throws InterruptedException {
    checkProducerFailure();
    final AtomicInteger integer = new AtomicInteger();
    ExecutorService executorService =
        Executors.newSingleThreadExecutor(r -> new Thread(r, "test-" + integer.getAndIncrement()));
    ExecutorService pool = Executors.newSingleThreadExecutor();
    CompletableFuture.runAsync(
            () -> System.out.println("first--" + Thread.currentThread().getName()), executorService)
        .thenRun(
            () -> {
              System.out.println("second--" + Thread.currentThread().getName());
              final int error = 1 / 0;
            })
        .thenRun(
            () -> {
              System.out.println("third--" + Thread.currentThread().getName());
            })
        .exceptionally(
            e -> {
              System.out.println("exception--" + Thread.currentThread().getName());
              executorService.shutdownNow();
              pool.shutdownNow();
              producerFailure = e;
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
                  TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                  throw new RuntimeException(e);
                }
              }
            },
            "checkErrorThread")
        .start();
  }
}
