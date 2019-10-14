package fun.enhui.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 使用Callable创建线程
 * @Author HuEnhui
 * @Date 2019/10/11 21:05
 **/
public class CallableDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        FutureTask<String> futureTask = new FutureTask(new Mythread());
        // FutureTask<String> futureTask = new FutureTask(()->{ System.out.println("come in callable");return "a";});

        new Thread(futureTask,"A").start();
        String result = futureTask.get();
        System.out.println(result);
    }
}

/**创建线程的4种方式
 *  1.继承 Thread 类
 *  2.实现 Runnable 接口
 *  3.实现 Callable 接口
 *  4.线程池
 * @author: HuEnhui
 * @date: 2019/10/11 21:24
  */
class Mythread implements Callable<String>
{
    @Override
    public String call() throws Exception
    {
        System.out.println("come in callable");
        return "a";
    }
}
