package com.enhui.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 最大内存容量 = 100 * 1024 * 1024 * （30 + 10） <br>
 * -Xmx4G -Xms4G <br>
 * -Xmx4300M -Xms4300M
 */
@Slf4j
public class TestGc {

  AtomicInteger integer = new AtomicInteger();
  List<Record> toSend;
  BlockingQueue<Record> toCommit = new ArrayBlockingQueue<>(30);

  public static void main(String[] args) throws InterruptedException {
    TestGc testGc = new TestGc();

    Thread thread =
        new Thread(
            () -> {
              while (true) {
                try {
                  TimeUnit.MILLISECONDS.sleep(1000L);
                  Record poll = testGc.toCommit.poll(1, TimeUnit.SECONDS);
                  if (poll == null) {
                    continue;
                  }
                  System.out.println("decrement：" + poll.getId() + "--" + testGc.toCommit.size());
                } catch (InterruptedException e) {
                  throw new RuntimeException(e);
                }
              }
            },
            "callback thread");
    thread.start();

    while (true) {
      testGc.toSend = testGc.poll();
      testGc.sendRecords();
    }
  }

  private void sendRecords() throws InterruptedException {
    for (Record record : toSend) {
      send(
          record,
          (id, e) -> {
            while (!toCommit.offer(record)) {
              TimeUnit.MILLISECONDS.sleep(1000L);
            }
            System.out.println(
                "increment："
                    + record.getId()
                    + "--"
                    + (long) toCommit.size()
                    + "--"
                    + (long) toSend.size());
          });
    }
    toSend = null;
  }

  private void send(Record record, Callback callback) throws InterruptedException {
    TimeUnit.MILLISECONDS.sleep(1L);
    callback.onCompletion(record.getId(), null);
  }

  private List<Record> poll() {
    List<Record> list = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      list.add(new Record(integer.incrementAndGet(), new byte[100 * 1024 * 1024]));
    }
    return list;
  }

  @Data
  @AllArgsConstructor
  public static class Record {
    private int id;
    private byte[] data;
  }

  public interface Callback {
    void onCompletion(Integer id, Exception exception) throws InterruptedException;
  }
}
