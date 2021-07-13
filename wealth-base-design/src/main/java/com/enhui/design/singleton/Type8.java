package com.enhui.design.singleton;

public class Type8 {
    public static void main(String[] args) {
        Singleton8 singleton = Singleton8.INSTANCE;
        Singleton8 singletonTwo = Singleton8.INSTANCE;
        System.out.println(singleton == singletonTwo);
        System.out.println(singleton.equals(singletonTwo));
        singleton.say();
    }
}

/**
 * 枚举实现单例模式
 * @author: HuEnhui
 * @date: 2019/12/19 14:43
 */
enum Singleton8{
    // 1.枚举类
    // 2.属性
    INSTANCE;
    public void say(){
        System.out.println("hello");
    }
}
