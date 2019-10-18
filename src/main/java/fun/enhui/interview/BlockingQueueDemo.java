package fun.enhui.interview;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * BlockingQueue阻塞队列
 * @Author: HuEnhui
 * @Date: 2019/10/18 14:13
 */
public class BlockingQueueDemo {
    public static void main(String[] args) {
        // 相当于 List list = new ArrayList();
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);

        // =========add() remove() 抛出异常======================
        for (int i = 0; i < 3; i++) {
            System.out.println(blockingQueue.add("a"));
        }
        // 队列满了在add抛出异常 Exception in thread "main" java.lang.IllegalStateException: Queue full
        // System.out.println(blockingQueue.add("d"));


        for (int i = 0; i < 3; i++) {
            System.out.println(blockingQueue.remove());
        }
        // 队列空了再remove抛出异常 Exception in thread "main" java.util.NoSuchElementException
        // System.out.println(blockingQueue.remove());


    }
}
