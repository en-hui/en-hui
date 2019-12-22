package fun.enhui.design.factory.factorymethod.pizzastroe.order;

import fun.enhui.design.factory.factorymethod.pizzastroe.factory.AbsFactoryMethod;
import fun.enhui.design.factory.factorymethod.pizzastroe.factory.CheesePizzaFactory;
import fun.enhui.design.factory.factorymethod.pizzastroe.factory.GreekPizzaFactory;
import fun.enhui.design.factory.factorymethod.pizzastroe.factory.PepperPizzaFactory;
import fun.enhui.design.factory.factorymethod.pizzastroe.pizza.Pizza;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 工厂模式的下单类
 * @author: HuEnhui
 * @date: 2019/12/22 15:24
 */
public class OrderPizza {

    public void orderPizza(){
        Pizza pizza;
        AbsFactoryMethod factoryMethod;
        do {
            String pizzaType = getType();
            // 根据类型创建不同工厂
            if ("cheese".equals(pizzaType)){
                factoryMethod = new CheesePizzaFactory();
                pizza = factoryMethod.createPizza();
            } else if ("greek".equals(pizzaType)){
                factoryMethod = new GreekPizzaFactory();
                pizza = factoryMethod.createPizza();
            }else if ("pepper".equals(pizzaType)){
                factoryMethod = new PepperPizzaFactory();
                pizza = factoryMethod.createPizza();
            }else {
                break;
            }
            // 打印
            pizza.prepare();
            pizza.bake();
            pizza.cut();
            pizza.box();
        }while (true);
    }


    /**
     * 获取客户输入的披萨种类
     * @author: HuEnhui
     * @date: 2019/12/19 16:55
     */
    private String getType(){
        BufferedReader strin = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("请输入pizza的种类");
        String str = null;
        try {
            str = strin.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return str;
    }
}
