package com.enhui.design.prototype.tradition;

/**
 * 传统模式实现克隆
 *
 * @Author: 胡恩会
 * @Date: 2020/6/27 15:23
 **/
public class Client {

    public static void main(String[] args) {
        Sheep sheep = new Sheep("小红", 2, "red");
        Sheep sheep1 = new Sheep(sheep.getName(), sheep.getAge(), sheep.getColor());
        Sheep sheep2 = new Sheep(sheep.getName(), sheep.getAge(), sheep.getColor());

        System.out.println(sheep);
        System.out.println(sheep1);
        System.out.println(sheep2);
    }
}
