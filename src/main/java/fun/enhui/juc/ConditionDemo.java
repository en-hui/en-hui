package fun.enhui.juc;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition练习
 * 三个线程A、B、C操作输出A、B、C
 * 三个线程顺序输出一次是一轮，根据n打印n次ABC
 * @Author HuEnhui
 * @Date 2019/10/11 20:20
 **/
public class ConditionDemo {
    public static void main(String[] args) {
        ShareData data = new ShareData();
        new Thread(()->{ for (int i = 0; i < 10; i++) data.printA(); },"A").start();
        new Thread(()->{ for (int i = 0; i < 10; i++) data.printB(); },"B").start();
        new Thread(()->{ for (int i = 0; i < 10; i++) data.printC(); },"C").start();
    }
}
class ShareData{
    /*
     *  标志位 A:1 B:2 C:3
     */
    private int flag =  1;
    private Lock lock = new ReentrantLock();
    Condition condition1 = lock.newCondition();
    Condition condition2 = lock.newCondition();
    Condition condition3 = lock.newCondition();
    public void printA(){
        lock.lock();
        try{
            while(flag!=1){
                condition1.await();
            }
            System.out.print("A");
            flag = 2;
            condition2.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }

    public void printB(){
        lock.lock();
        try{
            while(flag!=2){
                condition2.await();
            }
            System.out.print("B");
            flag = 3;
            condition3.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }

    public void printC(){
        lock.lock();
        try{
            while(flag!=3){
                condition3.await();
            }
            System.out.println("C");
            flag = 1;
            condition1.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }
}
