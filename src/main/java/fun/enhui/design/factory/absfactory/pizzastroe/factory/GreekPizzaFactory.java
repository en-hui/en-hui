package fun.enhui.design.factory.absfactory.pizzastroe.factory;

import fun.enhui.design.factory.absfactory.pizzastroe.pizza.BJGreekPizza;
import fun.enhui.design.factory.absfactory.pizzastroe.pizza.LDGreekPizza;
import fun.enhui.design.factory.absfactory.pizzastroe.pizza.Pizza;

public class GreekPizzaFactory extends AbsFactory {
    @Override
    public Pizza createBJPizza() {
        System.out.println("GreekPizzaFactory create BJGreekPizza");
        return new BJGreekPizza();
    }

    @Override
    public Pizza createLDPizza() {
        System.out.println("GreekPizzaFactory create LDGreekPizza");
        return new LDGreekPizza();
    }
}
