package fun.enhui.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;

/**
 * 编写一个可以从 1 到 n 输出代表这个数字的字符串的程序，但是：
 *
 *     如果这个数字可以被 3 整除，输出 "fizz"。
 *     如果这个数字可以被 5 整除，输出 "buzz"。
 *     如果这个数字可以同时被 3 和 5 整除，输出 "fizzbuzz"。
 *
 * 例如，当 n = 15，输出： 1, 2, fizz, 4, buzz, fizz, 7, 8, fizz, buzz, 11, fizz, 13, 14, fizzbuzz。
 *
 * 假设有这么一个类：
 *
 * class FizzBuzz {
 *   public FizzBuzz(int n) { ... }               // constructor
 *   public void fizz(printFizz) { ... }          // only output "fizz"
 *   public void buzz(printBuzz) { ... }          // only output "buzz"
 *   public void fizzbuzz(printFizzBuzz) { ... }  // only output "fizzbuzz"
 *   public void number(printNumber) { ... }      // only output the numbers
 * }
 *
 * 请你实现一个有四个线程的多线程版  FizzBuzz， 同一个 FizzBuzz 实例会被如下四个线程使用：
 *
 *     线程A将调用 fizz() 来判断是否能被 3 整除，如果可以，则输出 fizz。
 *     线程B将调用 buzz() 来判断是否能被 5 整除，如果可以，则输出 buzz。
 *     线程C将调用 fizzbuzz() 来判断是否同时能被 3 和 5 整除，如果可以，则输出 fizzbuzz。
 *     线程D将调用 number() 来实现输出既不能被 3 整除也不能被 5 整除的数字。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/fizz-buzz-multithreaded
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * @Author: HuEnhui
 * @Date: 2019/10/10 14:30
 */
public class FizzBuzzDemo {
    public static void main(String[] args) {
        Runnable printFizz = ()->{ System.out.print("fizz"); };
        Runnable printBuzz = ()->{ System.out.print("buzz"); };
        Runnable printFizzBuzz = ()->{ System.out.print("fizzbuzz"); };
        IntConsumer printNumber = (value)->{ System.out.print(value); };
        FizzBuzz fizzBuzz = new FizzBuzz(30);
        new Thread(()->{
            try {
                fizzBuzz.fizz(printFizz);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"A").start();
        new Thread(()->{
            try {
                fizzBuzz.buzz(printBuzz);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"B").start();
        new Thread(()->{
            try {
                fizzBuzz.fizzbuzz(printFizzBuzz);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"C").start();
        new Thread(()->{
            try {
                fizzBuzz.number(printNumber);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"D").start();

    }
}
/**
 *  其他人的解答--感觉不错的思路
 * @author: HuEnhui
 * @date: 2019/10/10 16:16
 * @return:
 */
/*class FizzBuzz {
    private int n;
    private volatile int state = -1;
    private Lock lock = new ReentrantLock();
    private Condition cond = lock.newCondition();

    public FizzBuzz(int n) {
        this.n = n;
    }

    // printFizz.run() outputs "fizz".
    public void fizz(Runnable printFizz) throws InterruptedException {
        for (int i = 3; i <= n; i += 3) {   //只输出3的倍数(不包含15的倍数)
            if (i % 15 == 0)	//15的倍数不处理，交给fizzbuzz()方法处理
                continue;
            lock.lock();
            while (state != 3) {
                cond.await();
            }
            printFizz.run();
            state = -1;	//控制权交还给number()方法
            cond.signalAll();	//全体起立
            lock.unlock();
        }
    }

    // printBuzz.run() outputs "buzz".
    public void buzz(Runnable printBuzz) throws InterruptedException {
        for (int i = 5; i <= n; i += 5) {   //只输出5的倍数(不包含15的倍数)
            if (i % 15 == 0)	//15的倍数不处理，交给fizzbuzz()方法处理
                continue;
            lock.lock();
            while (state != 5) {
                cond.await();
            }
            printBuzz.run();
            state = -1;	//控制权交还给number()方法
            cond.signalAll();	//全体起立
            lock.unlock();
        }
    }

    // printFizzBuzz.run() outputs "fizzbuzz".
    public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        for (int i = 15; i <= n; i += 15) {   //只输出15的倍数
            lock.lock();
            while (state != 15) {
                cond.await();
            }
            printFizzBuzz.run();
            state = -1;	//控制权交还给number()方法
            cond.signalAll();	//全体起立
            lock.unlock();

        }
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void number(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; ++i) {
            lock.lock();
            while (state != -1) {
                cond.await();
            }
            if (i % 3 != 0 && i % 5 != 0)
                printNumber.accept(i);
            else {
                if (i % 15 == 0)
                    state = 15;	//交给fizzbuzz()方法处理
                else if (i % 5 == 0)
                    state = 5;	//交给buzz()方法处理
                else
                    state = 3;	//交给fizz()方法处理

                cond.signalAll();	//全体起立
            }
            lock.unlock();
        }
    }
}*/
/**
 *  我的解答--超出时间限制
 * @author: HuEnhui
 * @date: 2019/10/10 16:16
 * @return:
 */
class FizzBuzz {
    private int n;

    public FizzBuzz(int n) {
        this.n = n;
    }
    private volatile int num = 1;
    private Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    // printFizz.run() outputs "fizz".
    public void fizz(Runnable printFizz) throws InterruptedException {
        lock.lock();
        try{
            while(num <=n){
                while(num<=n && num%3!=0 || num%5==0){
                    condition.await();
                }
                if(num>n){
                    break;
                }
                printFizz.run();
                num++;
                condition.signalAll();
            }

        }finally {
            lock.unlock();
        }

    }

    // printBuzz.run() outputs "buzz".
    public void buzz(Runnable printBuzz) throws InterruptedException {
        lock.lock();
        try{
            while(num<=n){
                while(num<=n && num%5!=0 || num%3==0){
                    condition.await();
                }
                if(num>n){
                    break;
                }
                printBuzz.run();
                num++;
                condition.signalAll();
            }
        }finally {
            lock.unlock();
        }

    }

    // printFizzBuzz.run() outputs "fizzbuzz".
    public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        lock.lock();
        try{
            while(num<=n){
                while(num<=n && (num%3!=0 || num%5!=0) ){
                    condition.await();
                }
                if(num>n){
                    break;
                }
                printFizzBuzz.run();
                num++;
                condition.signalAll();
            }

        }finally {
            lock.unlock();
        }

    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void number(IntConsumer printNumber) throws InterruptedException {
        lock.lock();
        try{
            while(num<=n){
                while(num<=n && (num%3==0 || num%5==0)){
                    condition.await();
                }
                if(num>n){
                    break;
                }
                printNumber.accept(num);
                num++;
                condition.signalAll();
            }
        }finally {
            lock.unlock();
        }

    }
}
