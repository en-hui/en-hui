package fun.enhui.design.factory.factorymethod.pizzastroe.factory;

import fun.enhui.design.factory.factorymethod.pizzastroe.pizza.PepperPizza;
import fun.enhui.design.factory.factorymethod.pizzastroe.pizza.Pizza;

public class PepperPizzaFactory extends AbsFactoryMethod {
    @Override
    public Pizza createPizza() {
        System.out.println("pepperPizza factory create PepperPizza");
        return new PepperPizza();
    }
}
