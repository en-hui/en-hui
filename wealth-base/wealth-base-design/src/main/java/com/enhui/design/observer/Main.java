package com.enhui.design.observer;

import java.util.concurrent.atomic.LongAdder;

/**
 * @Author 胡恩会
 * @Date 2020/6/18 21:40
 **/
public class Main {
    public static void main(String[] args) {
        Child child = new Child();
        child.add(new DogObserver());
        child.add(new FatherObserver());
        child.isCry();
        LongAdder a = new LongAdder();
        a.increment();

    }
}
