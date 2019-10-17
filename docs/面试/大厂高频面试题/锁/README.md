# Java锁
# Java锁之公平和非公平锁
**synchronized是非公平锁     
ReentrantLock的默认构造函数，创建非公平锁，等同于传参false;传参true的话，则创建公平锁**
- 公平锁        
多个线程按照申请锁的顺序来获取锁
- 非公平锁           
多线程获取锁的顺序并不是按照申请锁的顺序,有可能后申请的线程比先申请的线程优先获取到锁（线程直接尝试占有锁，尝试失败在采用类似公平锁的方式）   
**在高并发的情况下,有可能造成优先级反转或者饥饿现象，优点在于吞吐量比公平锁大**

# 可重入锁（又名递归锁）
**synchronized、ReentrantLock都是可重入锁**

> 可重入锁又名递归锁，是指在同一个线程在外层方法获取锁的时候，再进入该线程的内层方法会自动获取锁（前提锁对象得是同一个对象或者class），
不会因为之前已经获取过还没释放而阻塞，可重入锁的一个优点是可一定程度避免死锁。     
[可重入锁代码验证](https://github.com/Hu-enhui/study-code/blob/master/src/main/java/fun/enhui/interview/ReenterLockDemo.java)


# 自旋锁（spinlock）
自旋锁是指尝试获取锁的线程不会立即阻塞，而是**采用循环的方式去尝试获取锁**，这样的好处是减少线程上下文切换的消耗，缺点是循环会消耗cpu。
> 手写一个自旋锁: [使用AtomicReference和CAS实现自旋锁](https://github.com/Hu-enhui/study-code/blob/master/src/main/java/fun/enhui/interview/SpinLockDemo.java)             

# 独占锁（写锁）/共享锁（读锁）/互斥锁
- 独占锁：指该锁一次只能被一个线程所持有。         
**ReentrantLock和Synchronized都是独占锁**
- 共享锁：指该锁可被多个线程所持有。         
ReentrantReadWriteLock其读锁是共享锁，其写锁是独占锁。          
读锁的共享锁可以保证并发读是非常高效的，读写，写读，写写的过程都是互斥的。   
> 读写锁代码示例：[读写锁](https://github.com/Hu-enhui/study-code/blob/master/src/main/java/fun/enhui/interview/ReadWriteLockDemo.java)


