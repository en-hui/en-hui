package fun.enhui.thread.juc.interview;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者消费者 实例
 * 题目：多个线程操作初始值为0的一个变量
 * 实现一部分线程对该变量+1，一部分线程对该变量-1
 * 实现交替操作，十次循环，变量仍为0
 *
 * 1.高内聚低耦合前提下，线程操作资源类
 * 2.判断、干活、通知。判断不能使用if，使用while
 * 3.防止虚假唤醒
 * @Author HuEnhui
 * @Date 2019/10/9 20:14
 **/
public class ProduceConsumerDemo {
    public static void main(String[] args) throws Exception {
        Aircondition aircondition = new Aircondition();
        new Thread(()->{
            for (int j = 0; j <10 ; j++) {
                try {
                    aircondition.increment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },"A").start();

        new Thread(()->{
                for (int j = 0; j < 10; j++) {
                    try {
                        aircondition.devrement();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        },"B").start();
        new Thread(()->{
                for (int j = 0; j < 10; j++) {
                    try {
                        aircondition.increment();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        },"C").start();
        new Thread(()->{
                for (int j = 0; j < 10; j++) {
                    try {
                        aircondition.devrement();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        },"D").start();
    }
}

/**
 *  资源类
 * @author: HuEnhui
 * @date: 2019/10/9 20:16
  */
class Aircondition{
    private int number = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    // 使用lock的写法
    public void increment() throws Exception {
        lock.lock();
        try {
            // 1.判断
            while(number != 0){
                condition.await();
            }
            // 2.任务
            number++;
            System.out.println(Thread.currentThread().getName()+" "+number);
            // 3.通知
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }

    public void devrement() throws Exception {
        lock.lock();
        try{
            // 1.判断
            while(number == 0){
                condition.await();
            }
            // 2.任务
            number--;
            System.out.println(Thread.currentThread().getName()+" "+number);
            // 3.通知
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }
    // 使用synchronized的写法
    /*public synchronized void increment() throws Exception {
        // 1.判断
        while(number != 0){
            this.wait();
        }
        // 2.任务
        number++;
        System.out.println(Thread.currentThread().getName()+" "+number);
        // 3.通知
        this.notifyAll();
    }
    public synchronized void devrement() throws Exception {
        // 1.判断
        while(number == 0){
            this.wait();
        }
        // 2.任务
        number--;
        System.out.println(Thread.currentThread().getName()+" "+number);
        // 3.通知
        this.notifyAll();
    }*/
}
