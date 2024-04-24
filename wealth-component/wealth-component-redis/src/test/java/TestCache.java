import com.enhui.WealthRedissonClient;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

public class TestCache {

  @Test
  public void testAsync() throws InterruptedException {
    WealthRedissonClient client = new WealthRedissonClient();
    final CountDownLatch countDownLatch = new CountDownLatch(2);

    RedissonClient redissonClient = client.getRedissonClient();

    CompletableFuture.runAsync(
            () -> {
              RMap<String, String> map = redissonClient.getMap("test_redis");
              for (int i = 0; i < 10; i++) {
                map.put("node1-" + i, "success");
                System.out.println(Thread.currentThread().getName() + " " + map.entrySet());
                try {
                  TimeUnit.MILLISECONDS.sleep(100L);
                } catch (InterruptedException e) {
                  throw new RuntimeException(e);
                }
              }
            })
        .thenRun(
            () -> {
              RMap<String, String> map = redissonClient.getMap("test_redis");
              System.out.println(Thread.currentThread().getName() + " " + map.entrySet());

              map.clear();
              countDownLatch.countDown();
            })
        .exceptionally(
            e -> {
              e.printStackTrace();
              countDownLatch.countDown();
              return null;
            });

    CompletableFuture.runAsync(
            () -> {
              RMap<String, String> map = redissonClient.getMap("test_redis");
              for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + " " + map.entrySet());
                map.put("node2-" + i, "success");
                try {
                  TimeUnit.MILLISECONDS.sleep(300L);
                } catch (InterruptedException e) {
                  throw new RuntimeException(e);
                }
              }
            })
        .thenRun(
            () -> {
              RMap<String, String> map = redissonClient.getMap("test_redis");
              System.out.println(Thread.currentThread().getName() + " " + map.entrySet());

              map.clear();
              countDownLatch.countDown();
            })
        .exceptionally(
            e -> {
              e.printStackTrace();
              countDownLatch.countDown();
              return null;
            });

    countDownLatch.await();
    client.close();
  }
}
