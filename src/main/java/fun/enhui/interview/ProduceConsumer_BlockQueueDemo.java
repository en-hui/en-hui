package fun.enhui.interview;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用阻塞队列实现生产者消费者
 * @Author: HuEnhui
 * @Date: 2019/10/18 15:58
 */
public class ProduceConsumer_BlockQueueDemo {
    public static void main(String[] args) {
        MyResource myResource = new MyResource(new ArrayBlockingQueue(10));

        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"\t生产线程启动");
            try {
                myResource.produce();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"生产者").start();

        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"\t消费线程启动");
            try {
                myResource.consumer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"消费者").start();

        // 暂停一会线程
        try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }
        myResource.stop();
    }
}

/**
 *  我的消息队列
 * @author: HuEnhui
 * @date: 2019/10/18 16:19
 */
class MyResource{

    private volatile boolean FLAG = true;
    private AtomicInteger atomicInteger = new AtomicInteger();
    BlockingQueue<String> blockingQueue = null;

    /**
     *  构造方法，使用接口作为参数，可满足七个阻塞队列实现类
     * @author: HuEnhui
     * @date: 2019/10/18 16:17
     * @param blockingQueue
     * @return:
     */
    public MyResource(BlockingQueue blockingQueue) {
        this.blockingQueue = blockingQueue;
        System.out.println(blockingQueue.getClass().getName() + "\t实现类实现阻塞队列");
    }

    /**
     *  生产方法
     * @author: HuEnhui
     * @date: 2019/10/18 16:17
     * @param
     * @return: void
     */
    public void produce()throws Exception {
        String data = null;
        boolean retValue;
        while(FLAG){
            data = atomicInteger.incrementAndGet() + "";
            retValue = blockingQueue.offer(data,2L,TimeUnit.SECONDS);
            if(retValue){
                System.out.println(Thread.currentThread().getName()+"\t插入队列..."+data+"成功");
            }else{
                System.out.println(Thread.currentThread().getName()+"\t插入队列..."+data+"失败");
            }
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println("FLAG = false，生产停止了");
    }
    /**
     *  消费方法
     * @author: HuEnhui
     * @date: 2019/10/18 16:18
     * @param
     * @return: void
     */
    public void consumer()throws Exception {
        String result = null;
        while(FLAG) {
            result = blockingQueue.poll(2L,TimeUnit.SECONDS);
            if(null == result || "".equalsIgnoreCase(result)) {
                FLAG = false;
                System.out.println(Thread.currentThread().getName()+"\t超过2秒没有取到蛋糕，消息退出");

                return;
            }
            System.out.println(Thread.currentThread().getName()+"\t消费队列..."+result+"成功");
        }
    }

    /**
     *  停止
     * @author: HuEnhui
     * @date: 2019/10/18 16:19
     * @param
     * @return: void
     */
    public void stop() {
        this.FLAG = false;
    }
}
