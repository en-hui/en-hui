package com.enhui.design.proxy.jdk;

/**
 * @Author 胡恩会
 * @Date 2020/6/22 21:06
 **/
public class Tank implements Movable {

    @Override
    public void move() {
        System.out.println("坦克移动...");
    }
}
