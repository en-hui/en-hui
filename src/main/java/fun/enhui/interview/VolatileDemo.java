package fun.enhui.interview;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 1. 验证volatile保证可见性 visibility方法
 *   1.1 假如int number = 0;  number变量之前没有添加volatile关键字修饰，没有可见性
 *   1.2 假如volatile int number = 0;  number变量之前添加volatile关键字修饰，有可见性
 *
 * 2. 验证volatile不保证原子性 atomic方法
 *   2.1 使用volatile和i++ 多个线程操作后，出现写值丢失情况，不保证原子性
 *   2.2 使用AtomicInteger  保证原子性
 * @Author: HuEnhui
 * @Date: 2019/10/16 11:35
 */
public class VolatileDemo {
    public static void main(String[] args) {
        atomic();
    }

    /**
     *  volatile原子性
     * @author: HuEnhui
     * @date: 2019/10/16 15:12
     */
    private static void atomic() {
        MyData myData = new MyData();
        for (int i = 0; i < 20 ; i++) {
            new Thread(()->{
                for (int j = 0; j < 1000; j++) {
                    // 使用volatile和i++组合
                    myData.addPlusPlus();
                    // 使用AtomicInteger
                    myData.addAtomicPlusPlus();
                }
            },String.valueOf(i)).start();
        }
        //需要等待上面20个线程全部计算完成后，在用main线程取得最终结果
        // 默认有main线程和GC线程
        while(Thread.activeCount() > 2) {
            Thread.yield();
        }
        System.out.println(Thread.currentThread().getName()+"\t int type,finally number value:"+myData.number);
        System.out.println(Thread.currentThread().getName()+"\t AtomicInteger type,finally number value:"+myData.atomicInteger);

    }

    /**
     *  volatile可以保证可见性，及时通知其他线程，主物理内存中的值已经被修改。
     * @author: HuEnhui
     * @date: 2019/10/16 11:48
     */
    public static void visibility(){
        MyData myData = new MyData();
        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"\t come in");
            // 暂停一会A线程,给B线程运行提供机会
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myData.addTo10();
            System.out.println(Thread.currentThread().getName()+"\t update number value: " + myData.number);
        },"A").start();

        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"\t come in");
            // 第2个线程就是B线程
            while(myData.number == 0){
                // B线程一直循环，直到number不再等于0
            }
            System.out.println(Thread.currentThread().getName()+"\t mission is over,number value:" + myData.number);
        },"B").start();

    }
}

class MyData{
    volatile int  number = 0;

    public void addTo10(){
        this.number = 10;
    }

    /**
     * number由volatile关键字修饰，不保证原子性
     */
    public void addPlusPlus(){
        this.number++;
    }


    AtomicInteger atomicInteger = new AtomicInteger();
    public void addAtomicPlusPlus(){
        // 原子性i++
        atomicInteger.getAndIncrement();
    }
}


