package fun.enhui.thread.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池分为两大类
 * 一.ThreadPoolExecutor :
 *
 * <p>
 * 二.ForkJoinPool :
 * 1.分解汇总的任务（任务是分叉的，但结果要汇总）
 * 2.用很少的线程可以执行很多的任务（子任务） TPE 做不到先执行子任务
 * 3.CPU 密集型
 *
 * @Author 胡恩会
 * @Date 2020/8/27 23:12
 **/
public class T06_01_ThreadPool {
    public static void main(String[] args) {
        // 自定义线程池，共 7 个参数
        // 1.核心线程数 2.最大线程数 3.非核心线程空闲时间（生存时间） 4.生存时间的单位
        // 5.任务队列 6.线程工厂 7.拒绝策略
        ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 4,
                60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(4),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy());
    }
}
