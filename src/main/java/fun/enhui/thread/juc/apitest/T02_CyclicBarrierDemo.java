package fun.enhui.thread.juc.apitest;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier演示：
 * 1.初始化传递两个参数，屏障数量 和 要做的事
 * 2.每次调用await相当于+1
 * 3.加到屏障次数后，执行要做的事
 * API : public CyclicBarrier(int parties, Runnable barrierAction)
 * @Author: 胡恩会
 * @Date: 2019/10/18 10:12
 */
public class T02_CyclicBarrierDemo {
    public static void main(String[] args) {
        // 1.初始化
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7,() -> {
            System.out.println("****召唤神龙");
        });
        for (int i = 0; i < 7; i++) {
            final int tempInt = i;
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"\t收集到第"+tempInt+"颗龙珠");
                try {
                    // 每次调用相当于次数+1  次数到n后，执行主方法
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            },String.valueOf(i)).start();
        }
    }
}
