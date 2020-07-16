# Java锁
# 公平锁和非公平锁
**synchronized是非公平锁     
ReentrantLock的默认构造函数，创建非公平锁，等同于传参false;传参true的话，则创建公平锁**
- 公平锁        
多个线程按照申请锁的顺序来获取锁
- 非公平锁           
多线程获取锁的顺序并不是按照申请锁的顺序,有可能后申请的线程比先申请的线程优先获取到锁（线程直接尝试占有锁，尝试失败在采用类似公平锁的方式）      
在高并发的情况下,**有可能造成优先级反转或者饥饿现象**，优点在于吞吐量比公平锁大

# 可重入锁（又名递归锁）
**synchronized、ReentrantLock都是可重入锁**

> 可重入锁又名递归锁，是指在同一个线程在外层方法获取锁的时候，再进入该线程的内层方法会自动获取锁（前提锁对象得是同一个对象或者class），
不会因为之前已经获取过还没释放而阻塞，可重入锁的一个优点是可一定程度避免死锁。     
[可重入锁代码验证](https://github.com/Hu-enhui/study-code/blob/master/src/main/java/fun/enhui/interview/ReenterLockDemo.java)


# 自旋锁（spinlock）
自旋锁是指尝试获取锁的线程不会立即阻塞，而是**采用循环的方式去尝试获取锁**，这样的好处是减少线程上下文切换的消耗，缺点是循环会消耗cpu。
```java
/**
 * 使用 AtomicReference 和 cas 实现自旋锁
 * @Author: 胡恩会
 * @Date: 2019/10/17 17:37
 */
public class SpinLockDemo {
    /**
     * 原子引用线程
     **/
    AtomicReference<Thread> atomicReference = new AtomicReference<>();
    /**
     * 加锁
     **/
    public void myLock() {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + "\t尝试加锁。。。");
        // 主内存为null，则修改，否则循环等待
        while(!atomicReference.compareAndSet(null,thread)){

        }
    }
    /**
     * 解锁
     **/
    public void myUnLock() {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + "\t解锁。。。");
        atomicReference.compareAndSet(thread,null);
    }
    
    public static void main(String[] args) {
        SpinLockDemo spinLockDemo = new SpinLockDemo();
        new Thread(()->{
            spinLockDemo.myLock();
            System.out.println(Thread.currentThread().getName() + "\t获得锁，正在执行。。。");
            // 暂停一会线程
            try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }
            spinLockDemo.myUnLock();
        },"A").start();

        new Thread(()->{
            spinLockDemo.myLock();
            System.out.println(Thread.currentThread().getName() + "\t获得锁，正在执行。。。");
            // 暂停一会线程
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            spinLockDemo.myUnLock();
        },"B").start();
    }
}
```

# 互斥锁（写锁）/共享锁（读锁）
- 互斥锁：指该锁一次只能被一个线程所持有。          
 **ReentrantLock和Synchronized都是独占锁**   
- 共享锁：指该锁可被多个线程所持有。         
ReentrantReadWriteLock其读锁是共享锁，其写锁是互斥锁。          
读锁的共享锁可以保证并发读是非常高效的，读写，写读，写写的过程都是互斥的。   
```java
/**
 * 使用juc的 ReentrantReadWriteLock 读写锁案例
 * @Author: 胡恩会
 * @Date: 2020/7/1 22:18
 **/
public class ReadWriteLockDemo {
    public static void main(String[] args) {
        MyCaChe myCaChe = new MyCaChe();
        for (int i = 1; i <= 5; i++) {
            final int temp = i;
            new Thread(() -> {
                myCaChe.put(temp + "", temp);
            }, String.valueOf(i)).start();
        }
        for (int i = 1; i <= 5; i++) {
            int finalI = i;
            new Thread(() -> {
                myCaChe.get(finalI + "");
            }, String.valueOf(i)).start();
        }
    }
}
/**
 * 资源类
 */
class MyCaChe {
    /**
     * 保证可见性
     */
    private volatile Map<String, Object> map = new HashMap<>();
    private ReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    /**
     * 写
     */
    public void put(String key, Object value) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t正在写入" + key);
            //模拟网络延时
            try {
                TimeUnit.MICROSECONDS.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            map.put(key, value);
            System.out.println(Thread.currentThread().getName() + "\t正在完成");
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
    }
    /**
     * 读
     */
    public void get(String key) {
        reentrantReadWriteLock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t正在读取");
            //模拟网络延时
            try {
                TimeUnit.MICROSECONDS.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Object result = map.get(key);
            System.out.println(Thread.currentThread().getName() + "\t正在完成" + result);
        } finally {
            reentrantReadWriteLock.readLock().unlock();
        }
    }
    public void clearCaChe() {
        map.clear();
    }
}
```

