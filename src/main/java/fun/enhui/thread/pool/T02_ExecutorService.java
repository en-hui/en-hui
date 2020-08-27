package fun.enhui.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ExecutorService 接口
 *
 * @Author 胡恩会
 * @Date 2020/8/27 22:13
 **/
public class T02_ExecutorService {
    public static void main(String[] args) {
        // 创建一个线程池
        ExecutorService e = Executors.newCachedThreadPool();
    }
}
