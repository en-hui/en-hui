# synchronized 和 lock 锁

## synchronized 关键字
synchronized是可重入锁    
抛出异常时默认会释放锁    
> synchronized实现同步的基础：Java中的每一个对象都可以作为锁    
具体表现为以下3种形式:   
1.对于普通同步方法，锁是当前实例对象   
2.对于同步方法块，锁是 synchronized 括号里配置的对象   
3.对于静态同步方法，锁是当前类的Class对象   

> synchronized 锁升级过程：    
1.无锁   
2.偏向锁    
3.自旋锁   
4.重量级锁（向系统内核申请）     
synchronized不存在锁降级    
```
// 1.最初，只有一个线程访问该方法时，在this这个对象的头上(markword)记录这个线程的id
// 2.当多个线程过来出现争抢，没得到锁的线程自旋等待，默认自旋10次
// 3.当自旋次数达到10次或者自旋线程数达到？时，锁升级为重量级锁
synchronized(this){
    
}
```

## ReentrantLock 可重入锁
ReentrantLock 可以作为替代 synchronized 的技术。   

ReentrantLock的用法：    
```
Lock lock = new ReentrantLock();
public void method(){
    try {
         lock.lock();
         // 加锁的内容
    } catch (Exception e) {
         e.printStackTrace();
    }finally {
         lock.unlock();
    }
}

```

## synchronized 和 lock 有什么区别，用新的lock有什么好处？
1.原始构成
- synchronized 是关键字 属于JVM层面，    
    monitorenter（底层是通过monitor对象来完成，其实wait/notify等方法也依赖于monitor对象，只有在同步块或同步方法中才能调用wait/notify等方法）    
    monitorexit
- Lock是具体类（java.util.concurrent.locks.lock）是api层面的锁         

2.使用方法     
- synchronized 不需要用户手动释放锁，当synchronized代码执行完后系统会自动让线程释放对锁的占用      
- ReentrantLock则需要去手动释放锁，若没有手动释放锁，就有可能导致出现死锁的现象，
需要lock()和unLock()方法配合try/finally语句块来完成  

3.等待是否可中断
- synchronized不可中断，除非抛出异常或者正常运行完成
- ReentrantLock可中断，     
①设置超时方法trylock(long time,TimeUnit unit)     
②lockInterruptibly() 放代码块中，调用interrupt()方法可中断     

4.加锁是否公平
- synchronized 是非公平锁
- ReentrantLock两者都可以，默认是非公平锁，构造方法可以传入boolean值，true为公平锁，false为非公平锁

5.锁绑定多个条件Condition准确唤醒
- synchronized没有
- ReentrantLock 可以精确唤醒，而不是像synchronized要么随机唤醒一个线程，要么唤醒全部线程。
