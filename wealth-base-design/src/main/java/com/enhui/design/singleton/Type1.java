package com.enhui.design.singleton;

public class Type1 {
    public static void main(String[] args) {
        Singleton1 singleton = Singleton1.getInstance();
        Singleton1 singleton1 = Singleton1.getInstance();
        System.out.println(singleton == singleton1);
        System.out.println(singleton.equals(singleton1));
    }
}

/**
 *  饿汉式（静态常量）
 * @author: HuEnhui
 * @date: 2019/12/19 10:44
 */
class Singleton1{
    // 1.构造器私有化
    private Singleton1(){}
    // 2.创建静态对象
    private final static Singleton1 instance = new Singleton1();
    // 3.向外暴露一个静态的公共方法
    public static Singleton1 getInstance(){
        return instance;
    }
}
