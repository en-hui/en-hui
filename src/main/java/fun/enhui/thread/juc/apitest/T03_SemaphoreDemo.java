package fun.enhui.thread.juc.apitest;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Semaphore，资源循环使用
 * @Author: 胡恩会
 * @Date: 2019/10/18 11:14
 */
public class T03_SemaphoreDemo {
    public static void main(String[] args) {
        // 三个车位（三个资源）
        Semaphore semaphore = new Semaphore(3);
        // 六个车要找车位
        for (int i = 0; i < 6; i++) {
            new Thread(()->{
                try {
                    // 资源-1
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + "\t抢到车位");
                    // 停车三秒
                    try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
                    System.out.println(Thread.currentThread().getName() + "\t--离开车位");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    // 资源+1
                    semaphore.release();
                }
            },String.valueOf(i)).start();
        }
    }
}
