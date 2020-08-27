package fun.enhui.thread.pool;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * CompletableFuture 管理多个 future 的结果
 *
 * @Author 胡恩会
 * @Date 2020/8/27 22:55
 **/
public class T05_CompletableFuture {
    public static void main(String[] args) {
        Long start, end;
        start = System.currentTimeMillis();

        // 假设三个任务，去天猫，淘宝，京东 爬虫获取商品价格
        CompletableFuture<Double> futureTM = CompletableFuture.supplyAsync(() -> {
            T05_CompletableFuture.mySleep();
            return 2.0;
        });
        CompletableFuture<Double> futureTB = CompletableFuture.supplyAsync(() -> {
            T05_CompletableFuture.mySleep();
            return 1.0;
        });
        CompletableFuture<Double> futureJD = CompletableFuture.supplyAsync(() -> {
            T05_CompletableFuture.mySleep();
            return 3.0;
        });

        // allof 等待全部任务执行结束才向下执行
        CompletableFuture.allOf(futureTM, futureTB, futureJD).join();

        end = System.currentTimeMillis();
        System.out.println("use completable future " + (end - start));
    }

    /**
     * 随机睡眠
     *
     * @Author: 胡恩会
     * @Date: 2020/8/27 23:10
     * @return: void
     **/
    public static void mySleep() {
        Random random = new Random();
        int time = random.nextInt(10);
        try {
            TimeUnit.SECONDS.sleep(time);
            System.out.println(Thread.currentThread().getName() + " 休眠了 " + time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
