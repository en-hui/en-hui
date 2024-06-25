package com.enhui.juc;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class AQSTest {

  public static void main(String[] args) {
    AtomicReference<Thread> runningThread = new AtomicReference<>();
    Exception stacktrace = new RuntimeException();
    Future<String> future = null;

    ExecutorService executor =
        Executors.newSingleThreadExecutor(
            r -> {
              return new Thread(r, "test-juc-thread");
            });

    try {
      future =
          executor.submit(
              () -> {
                runningThread.set(Thread.currentThread());
                try {
                  TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                  throw new RuntimeException("interrupted ", e);
                }
                throw new RuntimeException("test exception");
              });

      future.get(2, TimeUnit.SECONDS);
    } catch (TimeoutException e) {
      future.cancel(true);
      if (runningThread.get() != null) {
        stacktrace.setStackTrace(runningThread.get().getStackTrace());
      }
      throw new RuntimeException("timeout.", stacktrace);
    } catch (Exception e) {
      if (future != null) {
        future.cancel(true);
      }
      throw new RuntimeException("failed.", e);
    } finally {
      executor.shutdownNow();
    }
  }
}
