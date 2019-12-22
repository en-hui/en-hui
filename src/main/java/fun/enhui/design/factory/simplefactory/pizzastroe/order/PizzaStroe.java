package fun.enhui.design.factory.simplefactory.pizzastroe.order;

import fun.enhui.design.factory.simplefactory.pizzastroe.factory.SimpleFactory;

/**
 * 相当于一个客户端，发出订购
 * @author: HuEnhui
 * @date: 2019/12/19 17:04
 */
public class PizzaStroe {

    public static void main(String[] args) {
        OrderPizza orderPizza = new OrderPizza();
        // 传统方式编码
        orderPizza.traditionWay();

        //使用简单工厂模式
        orderPizza.simpleFactory(new SimpleFactory());

    }
}
