package com.enhui.design.decorator;

/**
 * @Author 胡恩会
 * @Date 2020/6/25 10:48
 **/
public class Main {
    public static void main(String[] args) {
        TailDecorator tailDecorator = new TailDecorator(new Bullet());
        TailDecorator rectDecorator = new TailDecorator(new RectDecorator(new Tank()));
        rectDecorator.pagit();
        System.out.println();
        tailDecorator.pagit();
    }
}
