package com.enhui.design.factory.absfactory.pizzastroe.factory;

import fun.enhui.design.factory.absfactory.pizzastroe.pizza.BJCheesePizza;
import fun.enhui.design.factory.absfactory.pizzastroe.pizza.LDCheesePizza;
import fun.enhui.design.factory.absfactory.pizzastroe.pizza.Pizza;

public class CheesePizzaFactory extends AbsFactory {

    @Override
    public Pizza createBJPizza() {
        System.out.println("CheesePizzaFactory create BJCheesePizza");
        return new BJCheesePizza();
    }

    @Override
    public Pizza createLDPizza() {
        System.out.println("CheesePizzaFactory create LDCheesePizza");
        return new LDCheesePizza();
    }
}
