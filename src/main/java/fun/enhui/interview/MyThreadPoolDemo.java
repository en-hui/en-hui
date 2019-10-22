package fun.enhui.interview;

import java.util.concurrent.*;

/**
 * 线程池的使用，第四种使用线程的方式
 * @Author HuEnhui
 * @Date 2019/10/21 20:28
 **/
public class MyThreadPoolDemo {
    public static void main(String[] args) {
        // 查看本机电脑核数
        System.out.println(Runtime.getRuntime().availableProcessors());
    }

    /**
     *  自定义线程池，使用默认拒绝策略
     * @author: HuEnhui
     * @date: 2019/10/22 11:42
     * @param
     * @return: void
     */
    private static void abortPolicyWayPool() {

        ExecutorService threadPool = new ThreadPoolExecutor(
                2,
                5,
                1L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
        // 模拟十个用户来办理业务
        try{
            // 最大容纳请求数为8，因为线程数最大为5，等待队列长度为3，9则抛出异常，因为AbortPolicy默认拒绝策略的特点
            for (int i = 1; i <= 8; i++) {
                threadPool.execute(()->{
                    System.out.println(Thread.currentThread().getName()+"\t办理业务");
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            threadPool.shutdown();
        }
    }


    /**
     *  三种Executors提供的线程池
     * @author: HuEnhui
     * @date: 2019/10/22 11:35
     * @param
     * @return: void
     */
    private static void threadPoolInit() {
        // 一池有五个线程，假设银行有五个办理窗口
        ExecutorService threadPool1 = Executors.newFixedThreadPool(5);

        // 一池一个线程
        ExecutorService threadPool2 = Executors.newSingleThreadExecutor();

        // 一池N个线程，
        ExecutorService threadPool3 = Executors.newCachedThreadPool();

        // 模拟十个用户来办理业务
        try{
            for (int i = 1; i < 10; i++) {
                threadPool3.execute(()->{
                    System.out.println(Thread.currentThread().getName()+"\t办理业务");
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            threadPool3.shutdown();
        }
    }
}
