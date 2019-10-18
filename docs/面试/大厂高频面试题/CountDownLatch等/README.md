# JUC下CountDownLatch、CyclicBarrier、Semaphore
# CountDownLatch
让一些线程阻塞直到另外一些完成后才被唤醒
> 火箭发射倒计时；有一个很重要的事情要做，但是需要做一些前置工作，完成后才能执行这件重要的事。前置工作就好比倒计时。        
例如火箭发射，但火箭发射需要检测火箭各项数据，数据检测无误才可以发射火箭。
```
// 构造函数，给定一个次数
public CountDownLatch(int count)
// 相当于将次数-1
public void countDown()
// 次数为0之前，阻塞当前线程
public void await() throws InterruptedException 
```
代码示例：[CountDownLatch演示](https://github.com/Hu-enhui/study-code/blob/master/src/main/java/fun/enhui/interview/CountDownLatchDemo.java)


# CyclicBarrier
CyclicBarrier的字面意思是可循环(Cyclic)使用的屏障(barrier).
它要做的事情是,让一组线程到达一个屏障(也可以叫做同步点)时被阻塞,直到最后一个线程到达屏障时,屏障才会开门,
所有被屏障拦截的线程才会继续干活,线程进入屏障通过CyclicBarrier的await()方法.
> 与CountDownLatch类似，CountDownLatch做减法，初始值为n，减到0执行主方法    
CyclicBarrier做加法，初始值为0，加到n执行主方法
```
// 构造函数，给定一个次数，一个主方法（实现Runnable接口的类的对象）
public CyclicBarrier(int parties, Runnable barrierAction)
// 每次调用相当于次数+1  次数到n后，执行主方法
public int await() throws InterruptedException, BrokenBarrierException
```
代码示例：[CyclicBarrier演示](https://github.com/Hu-enhui/study-code/blob/master/src/main/java/fun/enhui/interview/CyclicBarrierDemo.java)

# Semaphore（信号量）
信号量主要用于两个目的
1. 用于多个共享资源的互斥使用
2. 用于并发线程数的控制
```
// 构造函数，给定一个资源个数（资源可以循环使用）  eg:停车位个数
public Semaphore(int permits) 
// 每次调用相当于资源数-1     eg：停车位被占，车位数-1
public void acquire() throws InterruptedException
// 每次调用相当于资源数+1     eg：车离开车位，车位数+1
public void release()
```
代码示例：[Semaphore演示](https://github.com/Hu-enhui/study-code/blob/master/src/main/java/fun/enhui/interview/SemaphoreDemo.java)
