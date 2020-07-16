# 阻塞队列

# 阻塞队列理论
阻塞队列,顾名思义,首先它是一个队列,而一个阻塞队列在数据结构中所起的作用大致如图所示:    
![Alt](img/阻塞队列.png)       
**线程1往阻塞队列中添加元素二线程2从队列中移除元素**       
当阻塞队列是空时,从队列中**获取**元素的操作将会被阻塞.      
当阻塞队列是满时,往队列中**添加**元素的操作将会被阻塞.      
eg：蛋糕店货柜中能装十个蛋糕，一直做蛋糕，直到装满了就不在做蛋糕了，但是卖掉一个或多个，就开始做，卖完就不能卖了

# 为什么用阻塞队列，有什么优点
在多线程领域:所谓阻塞,在某些情况下会挂起线程(即线程阻塞),一旦条件满足,被挂起的线程又会被自动唤醒
 
为什么需要使用BlockingQueue？   
好处是我们不需要关心什么时候需要阻塞线程,什么时候需要唤醒线程,因为BlockingQueue都一手给你包办好了
也就是说不用再手动 wait 和 notify
 
在concurrent包 发布以前,在多线程环境下,我们每个程序员都必须自己去控制这些细节,尤其还要兼顾效率和线程安全,而这会给我们的程序带来不小的复杂度.

# BlockingQueue接口结构和实现类
BlockingQueue是和List同级的接口，只需要重点掌握三个实现类即可，接口架构如下图：    
![Alt](img/阻塞队列接口架构.png) 
- **ArrayBlockingQueue** —— 由数组结构组成的有界阻塞队列
- **LinkedBlockingQueue** —— 由链表结构组成的有界（但大小默认值为Integer.MAX_VALUE）阻塞队列
- PriorityBlockingQueue —— 支持优先级排序的无界阻塞队列
- DelayQueue —— 使用优先级队列实现的延迟无界阻塞队列
- **SynchronousQueue** —— 不存储元素的阻塞队列，也即单个元素的队列
- LinkedTransferQueue —— 由链表结构组成的无界阻塞队列
- LinkedBlockingDeque —— 由链表结构组成的双向阻塞队列

# BlockingQueue的核心方法：     
![Alt](img/核心方法.png) 

| 方法组特性     |                             含义解释                             |
| ---- | :---: |
|   抛出异常   | 当阻塞队列满时,再往队列里面add插入元素会抛IllegalStateException: Queue full<br/>当阻塞队列空时,再往队列Remove元素时候回抛出NoSuchElementException |
| 特殊值 | 插入方法,成功返回true 失败返回false<br/>移除方法,成功返回元素,队列里面没有就返回null |
| 一直阻塞 | 当阻塞队列满时,生产者继续往队列里面put元素,队列会一直阻塞直到put数据or响应中断退出<br/>当阻塞队列空时,消费者试图从队列take元素,队列会一直阻塞消费者线程直到队列可用. |
| 超时退出 | 当阻塞队列满时,队列会阻塞生产者线程一定时间,超过后限时后生产者线程就会退出 |

代码示例:[以抛出异常组为例理解上面表格](https://github.com/Hu-enhui/study-code/blob/master/src/main/java/fun/enhui/interview/BlockingQueueDemo.java)

# 阻塞队列之同步SynchronousQueue队列
> 创建一个SynchronousQueue对象，两个线程同时操作此对象，一个线程往队列里塞值，另一个队列从队列里取值。    
这个类的特点:每一次塞值只能塞一个，当队列里有元素，塞值线程就会阻塞，同理，如果队列里没有值，取值线程也会阻塞   
代码示例:[只能有一个元素的阻塞队列SynchronousQueue](https://github.com/Hu-enhui/study-code/blob/master/src/main/java/fun/enhui/interview/SynchronousQueueDemo.java)
  
# 使用场景
- 生产者消费者模式（原理东西，掌握）
- 线程池
- 消息中间件

# 生产者消费者模式
## 传统版
- 用 synchronized 加锁，wait 和 notify 操作
- 用 ReentrantLock 加锁，await 和 signal 操作      
代码示例:[生产者消费者传统版](https://github.com/Hu-enhui/study-code/blob/master/src/main/java/fun/enhui/juc/ProduceConsumerDemo.java)
## 阻塞队列版
代码示例：[生产者消费者阻塞队列版](https://github.com/Hu-enhui/study-code/blob/master/src/main/java/fun/enhui/interview/ProduceConsumer_BlockQueueDemo.java)

