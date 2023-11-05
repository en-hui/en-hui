package com.enhui.design.singleton;

public class Type2 {
    public static void main(String[] args) {
        Singleton2 singleton = Singleton2.getInstance();
        Singleton2 singleton1 = Singleton2.getInstance();
        System.out.println(singleton == singleton1);
        System.out.println(singleton.equals(singleton1));
    }
}

/**
 *  饿汉式（静态代码块）
 * @author: HuEnhui
 * @date: 2019/12/19 10:44
 */
class Singleton2{
    // 1.私有构造器
    private Singleton2(){}
    // 2.类的内部创建静态对象
    private static Singleton2 instance;
    // 3.静态代码块实例化
    static{
        instance = new Singleton2();
    }
    // 4.公共方法返回实例对象
    public static Singleton2 getInstance(){
        return instance;
    }
}
