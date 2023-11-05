package com.enhui.design.factory.factorymethod.pizzastroe.factory;

import com.enhui.design.factory.factorymethod.pizzastroe.pizza.PepperPizza;
import com.enhui.design.factory.factorymethod.pizzastroe.pizza.Pizza;

public class PepperPizzaFactory extends AbsFactoryMethod {
    @Override
    public Pizza createPizza() {
        System.out.println("pepperPizza factory create PepperPizza");
        return new PepperPizza();
    }
}
