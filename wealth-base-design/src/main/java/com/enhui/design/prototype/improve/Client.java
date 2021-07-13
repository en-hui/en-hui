package com.enhui.design.prototype.improve;

public class Client {
    public static void main(String[] args) {
        Sheep sheep = new Sheep("小红",2,"red");
        Sheep sheep1 = (Sheep) sheep.clone();
        Sheep sheep2 = (Sheep)sheep.clone();

        System.out.println(sheep);
        System.out.println(sheep1);
        System.out.println(sheep2);

    }
}
