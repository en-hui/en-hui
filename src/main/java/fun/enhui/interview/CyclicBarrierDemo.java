package fun.enhui.interview;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier演示，作用：初始化传递两个参数，屏障数量 和 要做的事   每次调用await相当于+1，加到屏障次数后，执行要做的事
 * API : public CyclicBarrier(int parties, Runnable barrierAction)
 * @Author: HuEnhui
 * @Date: 2019/10/18 10:12
 */
public class CyclicBarrierDemo {

    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7,() -> {
            System.out.println("****召唤神龙");
        });
        for (int i = 0; i < 7; i++) {
            final int tempInt = i;
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"\t收集到第"+tempInt+"颗龙珠");
                try {
                    //
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
