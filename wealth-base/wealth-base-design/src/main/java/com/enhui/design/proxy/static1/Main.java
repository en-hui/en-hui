package com.enhui.design.proxy.static1;

/**
 * @Author 胡恩会
 * @Date 2020/6/22 20:57
 **/
public class Main {
    public static void main(String[] args) {
        new LogProxy(new Tank()).move();
    }
}
