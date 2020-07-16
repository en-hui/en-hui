package fun.enhui.thread.juc.interview;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 现在有两种线程，氢 oxygen 和氧 hydrogen，你的目标是组织这两种线程来产生水分子。
 *
 * 存在一个屏障（barrier）使得每个线程必须等候直到一个完整水分子能够被产生出来。
 *
 * 氢和氧线程会被分别给予 releaseHydrogen 和 releaseOxygen 方法来允许它们突破屏障。
 *
 * 这些线程应该三三成组突破屏障并能立即组合产生一个水分子。
 *
 * 你必须保证产生一个水分子所需线程的结合必须发生在下一个水分子产生之前。
 *
 * 换句话说:
 *
 *     如果一个氧线程到达屏障时没有氢线程到达，它必须等候直到两个氢线程到达。
 *     如果一个氢线程到达屏障时没有其它线程到达，它必须等候直到一个氧线程和另一个氢线程到达。
 *
 * 书写满足这些限制条件的氢、氧线程同步代码。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/building-h2o
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * @Author: HuEnhui
 * @Date: 2019/10/10 14:04
 */
public class H2ODemo {
    public static void main(String[] args) {
        Runnable hRun = ()->{ System.out.print("H"); };
        Runnable oRun = ()->{ System.out.print("O"); };
        int n = 2;
        H2O h2O = new H2O();
        new Thread(()->{
            try {
                for (int i = 0; i <2*n ; i++) {
                    h2O.hydrogen(hRun);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"A").start();

        new Thread(()->{
            try {
                for (int i = 0; i <n ; i++) {
                    h2O.oxygen(oRun);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}


/**
 * 资源类
 */
class H2O {

    public H2O() {

    }

    private volatile int h=0;
    private volatile int o=0;
    private Lock lock = new ReentrantLock();
    private Condition hCondition = lock.newCondition();
    private Condition oCondition = lock.newCondition();
    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        lock.lock();
        try{
            while(h==2 && o!=1){
                hCondition.await();
            }

            // releaseHydrogen.run() outputs "H". Do not change or remove this line.
            releaseHydrogen.run();
            h++;
            oCondition.signalAll();
            while(h==2 && o==1){
                h=0;
                o=0;
            }
        }finally{
            lock.unlock();
        }

    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        lock.lock();
        try{
            while(h!=2 && o==1){
                oCondition.await();
            }
            // releaseOxygen.run() outputs "O". Do not change or remove this line.
            releaseOxygen.run();
            o++;
            hCondition.signalAll();
            while(h==2 && o==1){
                h=0;
                o=0;
            }
        }finally{
            lock.unlock();
        }

    }
}
