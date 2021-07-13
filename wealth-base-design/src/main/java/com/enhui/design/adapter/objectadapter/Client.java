package com.enhui.design.adapter.objectadapter;

public class Client {
    public static void main(String[] args) {
        System.out.println("===对象适配器===");
        Phone phone = new Phone();
        VoltageAdapter adapter = new VoltageAdapter(new Voltage220V());
        phone.charging(adapter);
    }
}
