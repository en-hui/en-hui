package fun.enhui.design.factory.absfactory.pizzastroe.factory;

import fun.enhui.design.factory.absfactory.pizzastroe.pizza.BJPepperPizza;

import fun.enhui.design.factory.absfactory.pizzastroe.pizza.LDPepperPizza;
import fun.enhui.design.factory.absfactory.pizzastroe.pizza.Pizza;

public class PepperPizzaFactory extends AbsFactory {

    @Override
    public Pizza createBJPizza() {
        System.out.println("PepperPizzaFactory create BJPepperPizza");
        return new BJPepperPizza();
    }

    @Override
    public Pizza createLDPizza() {
        System.out.println("PepperPizzaFactory create LDPepperPizza");
        return new LDPepperPizza();
    }
}
