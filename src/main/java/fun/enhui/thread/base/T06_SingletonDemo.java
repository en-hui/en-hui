package fun.enhui.thread.base;

/**
 * 加入volatile的DCL单例模式
 *
 * @Author: HuEnhui
 * @Date: 2019/10/16 16:43
 */
public class T06_SingletonDemo {
    private static volatile T06_SingletonDemo instance = null;

    private T06_SingletonDemo() {
        System.out.println(Thread.currentThread().getName() + "构造方法");
    }

    // DCL （Double Check Lock 双端检锁机制）
    public static T06_SingletonDemo getInstance() {
        if (instance == null) {
            synchronized (T06_SingletonDemo.class) {
                if (instance == null) {
                    instance = new T06_SingletonDemo();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                T06_SingletonDemo.getInstance();
            }, String.valueOf(i)).start();
        }
    }
}
