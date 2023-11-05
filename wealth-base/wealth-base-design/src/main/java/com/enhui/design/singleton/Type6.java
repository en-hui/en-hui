package com.enhui.design.singleton;

public class Type6 {
    public static void main(String[] args) {
        Singleton6 singleton = Singleton6.getInstance();
        Singleton6 singletonTwo = Singleton6.getInstance();
        System.out.println(singleton == singletonTwo);
        System.out.println(singleton.equals(singletonTwo));
    }
}

/**
 *  双重检查
 * @author: HuEnhui
 * @date: 2019/12/19 14:10
 */
class Singleton6{
    // 1.私有构造器
    private Singleton6(){}
    // 2.创建私有静态轻量同步变量
    private static volatile Singleton6 instance;
    // 3.提供公共静态方法
    public static Singleton6 getInstance(){
        // 4.方法内 判断+同步+判断 保证线程安全
        if (instance == null){
            synchronized (Singleton6.class){
                if (instance == null){
                    instance = new Singleton6();
                }
            }
        }
        return instance;
    }
}
