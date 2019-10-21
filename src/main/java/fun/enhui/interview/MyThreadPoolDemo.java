package fun.enhui.interview;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池的使用，第四种使用线程的方式
 * @Author HuEnhui
 * @Date 2019/10/21 20:28
 **/
public class MyThreadPoolDemo {
    public static void main(String[] args) {
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
