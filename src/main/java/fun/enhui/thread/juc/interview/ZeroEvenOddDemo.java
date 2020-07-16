package fun.enhui.thread.juc.interview;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;

/**
 * 线程 A 将调用 zero()，它只输出 0 。
 * 线程 B 将调用 even()，它只输出偶数。
 * 线程 C 将调用 odd()，它只输出奇数。
 * 例如：输入：n = 2    输出："0102"
 * @Author: HuEnhui
 * @Date: 2019/10/10 11:21
 */
public class ZeroEvenOddDemo {
    public static void main(String[] args)throws Exception {
        IntConsumer intConsumer = (int value)->{
            System.out.print(value);
        };

        ZeroEvenOdd zeroEvenOdd = new ZeroEvenOdd(3);
        // 三个线程分别调用输出0、奇数、偶数
        new Thread(()->{
            try {
                zeroEvenOdd.zero(intConsumer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"A").start();
        new Thread(()->{
            try {
                zeroEvenOdd.even(intConsumer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"B").start();
        new Thread(()->{
            try {
                zeroEvenOdd.odd(intConsumer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"C").start();
    }

}
/**
 * 资源类
 */
class ZeroEvenOdd{
    private int n;

    public ZeroEvenOdd(int n) {
        this.n = n;
    }
    // 01 02表示输出0，下次输出奇、偶   10  20表示输出奇、偶，下次输出0
    private String flag = "01";
    private volatile int num = 0;
    private Lock lock = new ReentrantLock();
    private Condition zeroCondition = lock.newCondition();
    private Condition evenCondition = lock.newCondition();
    private Condition oddCondition = lock.newCondition();
    // printNumber.accept(x) outputs "x", where x is an integer.
    /**
     *  打印0
     * @author: HuEnhui
     * @date: 2019/10/10 13:13
     * @param printNumber
     * @return: void
     */
    public void zero(IntConsumer printNumber) throws InterruptedException {
        lock.lock();
        try{
            for(int i =0;i<n;i++){
                while(! ("01".equals(flag) || "02".equals(flag)) ) {
                    zeroCondition.await();
                }
                printNumber.accept(0);

                num++;
                if("01".equals(flag)) {
                    flag = "10";
                    oddCondition.signalAll();
                }else if("02".equals(flag)) {
                    flag = "20";
                    evenCondition.signalAll();
                }
            }

        }finally{
            lock.unlock();
        }
    }

    /**
     *  打印偶数
     * @author: HuEnhui
     * @date: 2019/10/10 13:14
     * @param printNumber
     * @return: void
     */
    public void even(IntConsumer printNumber) throws InterruptedException {
        lock.lock();
        try{
            for(int i = 0;i<n/2;i++){
                while(!"20".equals(flag)){
                    evenCondition.await();
                }
                printNumber.accept(num);
                flag = "01";
                zeroCondition.signalAll();
            }

        }finally{
            lock.unlock();
        }
    }

    /**
     *  打印奇数
     * @author: HuEnhui
     * @date: 2019/10/10 13:14
     * @param printNumber
     * @return: void
     */
    public void odd(IntConsumer printNumber) throws InterruptedException {
        lock.lock();
        try{
            for(int i=0;i<(n+1)/2;i++){
                while(!"10".equals(flag)){
                    oddCondition.await();
                }
                printNumber.accept(num);
                flag = "02";
                zeroCondition.signalAll();
            }

        }finally{
            lock.unlock();
        }
    }
}
