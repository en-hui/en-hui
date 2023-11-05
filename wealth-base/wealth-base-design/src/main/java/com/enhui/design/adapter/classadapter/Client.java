package com.enhui.design.adapter.classadapter;

public class Client {
    public static void main(String[] args) {
        System.out.println("===类适配器===");
        Phone phone = new Phone();
        phone.charging(new VoltageAdapter());
    }
}
