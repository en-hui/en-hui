package fun.enhui.juc;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 使用juc包的 CopyOnWriteArrayList、CopyOnWriteArraySet、ConcurrentHashMap
 * @Author HuEnhui
 * @Date 2019/10/8 19:41
 *
 * 1.故障现象
 *   java.util.ConcurrentModificationException  并发修改异常
 * 2.导致原因
 *
 * 3.解决方法
 *   3.1 new Vector<>() 使用加锁的线程安全类
 *   3.2 Collections.synchronizedList(new ArrayList<>())  使用Collections工具类
 *   3.3 new CopyOnWriteArrayList<>()  写时复制
 **/
public class CopyOnWriteDemo {
    public static void main(String[] args) {
        mapNotSafe();
    }

    /**
     *  HashMap线程不安全，ConcurrentHashMap
     * @author: HuEnhui
     * @date: 2019/10/8 20:48
     * @param
      * @return: void
      */
    private static void mapNotSafe() {
        Map<String,String> map = new ConcurrentHashMap<>();
        for (int i = 0; i <30 ; i++) {
            new Thread(()->{
                map.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0,8));
                System.out.println(map);
            },String.valueOf(i)).start();
        }
    }

    /**
     *  HashSet线程不安全，CopyOnWriteArraySet
     * @author: HuEnhui
     * @date: 2019/10/8 20:43
     * @param
      * @return: void
      */
    private static void setNotSafe() {
        Set<String> set = new CopyOnWriteArraySet<>();
        for (int i = 0; i <30 ; i++) {
            new Thread(()->{
                set.add(UUID.randomUUID().toString().substring(0,8));
                System.out.println(set);
            },String.valueOf(i)).start();
        }
    }

    /**
     *  ArrayList线程不安全，CopyOnWriteArrayList
     * @author: HuEnhui
     * @date: 2019/10/8 20:36
     * @param
      * @return: void
      */
    private static void listNotSafe() {
        List<String> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i <30 ; i++) {
            new Thread(()->{
                list.add(UUID.randomUUID().toString().substring(0,8));
                System.out.println(list);
            },String.valueOf(i)).start();
        }
    }
}
