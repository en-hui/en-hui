package fun.enhui.thread.pool;

import java.util.concurrent.*;

/**
 * Callable 接口
 * 类似Runnable接口，有返回结果
 *
 * @Author 胡恩会
 * @Date 2020/8/27 22:19
 **/
public class T03_Callable {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 定义一个任务，由线程池执行
        Callable<String> callable = () -> {
            TimeUnit.SECONDS.sleep(2);
            return "Hello callable，callable执行结束";
        };

        // 创建一个线程池
        ExecutorService service = Executors.newCachedThreadPool();
        // 将任务 callable 交给线程池（异步执行）,由 future 管理结果
        Future<String> future = service.submit(callable);

        System.out.println("验证 submit 是异步执行-> 不需要等callable执行结束");

        // get 方法是阻塞的
        System.out.println(future.get());

        service.shutdown();
    }
}
