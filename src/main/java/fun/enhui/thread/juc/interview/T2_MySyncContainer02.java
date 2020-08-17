package fun.enhui.thread.juc.interview;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 2.写一个固定容量的同步容器，拥有put和get方法，能够支持2个生产者线程以及10个消费者线程的阻塞调用
 *
 * @author 胡恩会
 * @date 2020/8/148:29
 */
public class T2_MySyncContainer02 {
    public static void main(String[] args) {
        MySyncContainer02<String> list = new MySyncContainer02();
        // 消费者线程
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    System.out.println(list.get());
                }
            }, "c" + i).start();

        }

        // 生产者线程
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                for (int j = 0; j < 25; j++) {
                    list.put(Thread.currentThread().getName() + " " + j);
                }
            }, "p" + i).start();

        }

    }
}

/**
 * 我的同步容器
 *
 * @param <T>
 */
class MySyncContainer02<T> {
    Lock lock = new ReentrantLock();

    Condition producer = lock.newCondition();

    Condition consumer = lock.newCondition();

    final private LinkedList<T> lists = new LinkedList<T>();

    final private int MAX = 10;

    public void put(T t) {
        try {
            lock.lock();
            while (lists.size() == MAX) {
                producer.await();
            }
            lists.add(t);
            consumer.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    public T get() {
        T t = null;
        try {
            lock.lock();
            while (lists.size() == 0) {
                consumer.await();
            }
            t = lists.removeFirst();
            producer.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            lock.unlock();
        }
        return t;
    }
}