package fun.enhui.thread.pool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * Future 接口，存储执行任务产生的结果
 * FutureTask 接口，就是 Runnable + Future
 *
 * @Author 胡恩会
 * @Date 2020/8/27 22:24
 **/
public class T04_Future {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // futureTask 即管理任务，又管理结果
        FutureTask<Integer> task = new FutureTask<>(() -> {
            TimeUnit.MILLISECONDS.sleep(500);
            return 1000;
        });

        new Thread(task).start();
        System.out.println(task.get());

    }
}
