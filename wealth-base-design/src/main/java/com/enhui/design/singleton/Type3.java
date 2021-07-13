package com.enhui.design.singleton;

public class Type3 {
    public static void main(String[] args) {
        Singleton3 singleton = Singleton3.getInstance();
        Singleton3 singletonTwo = Singleton3.getInstance();
        System.out.println(singleton == singletonTwo);
        System.out.println(singleton.equals(singletonTwo));
    }
}

/**
 * 懒汉式（线程不安全）
 * @author: HuEnhui
 * @date: 2019/12/19 13:26
 */
class Singleton3{
    // 1.私有构造器
    private Singleton3(){}
    // 2.创建静态对象
    private static Singleton3 instance;
    // 3。提供一个公共方法
    // 懒汉式 - 在第一次调用该方法时实例化对象
    public static Singleton3 getInstance() {
        if (instance == null) {
            instance = new Singleton3();
        }
        return instance;
    }
}
