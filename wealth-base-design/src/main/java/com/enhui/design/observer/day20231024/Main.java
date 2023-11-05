package com.enhui.design.observer.day20231024;

public class Main {
    public static void main(String[] args) {
        Child child = new Child();
        child.addObserver(new DogObserver());
        child.addObserver(new FatherObserver());

        child.isCry();
    }
}
