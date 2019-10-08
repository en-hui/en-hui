package fun.enhui.juc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用可重入锁实现卖票业务
 * @Author HuEnhui
 * @Date 2019/9/29 22:54
 **/
public class ReentrantLockTest {

    public static void main(String[] args) {
        Ticket ticket = new Ticket();
        // 匿名内部类使用lamada表达式更加简洁 ()->{}
        new Thread(()->{ for(int i=0;i<40;i++) { ticket.sale(); }},"A").start();
        new Thread(()->{ for(int i=0;i<40;i++) { ticket.sale(); }},"B").start();
        new Thread(()->{ for(int i=0;i<40;i++) { ticket.sale(); }},"C").start();
    }
}
/**
 * 票
 * @Author: HuEnhui
 * @Date: 2019/9/29 8:59
 */
class Ticket {
    private int number = 30;
    Lock lock = new ReentrantLock();

    /**
     *  卖票
     * @author: HuEnhui
     * @date: 2019/9/29 23:03
     * @param
      * @return: void
      */
    public void sale() {
        lock.lock();
        try {
            if (number > 0) {
                System.out.println(Thread.currentThread().getName() + "卖出票" + number-- + "，剩余" + number + "张票");
            }
        } finally {
            lock.unlock();
        }
    }
}
