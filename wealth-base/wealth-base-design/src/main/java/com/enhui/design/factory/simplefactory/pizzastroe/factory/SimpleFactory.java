package com.enhui.design.factory.simplefactory.pizzastroe.factory;


import com.enhui.design.factory.simplefactory.pizzastroe.pizza.CheesePizza;
import com.enhui.design.factory.simplefactory.pizzastroe.pizza.GreekPizza;
import com.enhui.design.factory.simplefactory.pizzastroe.pizza.PepperPizza;
import com.enhui.design.factory.simplefactory.pizzastroe.pizza.Pizza;

/**
 * 简单工厂类
 * @author: HuEnhui
 * @date: 2019/12/19 17:51
 */
public class SimpleFactory {

	/**
	 * 根据下单类型 返回对应的 披萨 对象
	 * @author: HuEnhui
	 * @date: 2019/12/19 17:51
	 */
	public Pizza createPizza(String orderType) {

		Pizza pizza = null;

		System.out.println("使用简单工厂模式");
		if (orderType.equals("greek")) {
			pizza = new GreekPizza();
			pizza.setName(" 希腊披萨 ");
		} else if (orderType.equals("pepper")) {
			pizza = new PepperPizza();
			pizza.setName("胡椒披萨");
		} else if (orderType.equals("cheese")) {
			pizza = new CheesePizza();
			pizza.setName(" 奶酪披萨 ");
		}
		return pizza;
	}

	/**
	 *  简单工厂模式 也叫静态工厂模式.
	 * @author: HuEnhui
	 * @date: 2019/12/19 17:53
	 */
	public static Pizza staticCreatePizza(String orderType) {

		Pizza pizza = null;

		System.out.println("使用简单工厂模式");
		if (orderType.equals("greek")) {
			pizza = new GreekPizza();
			pizza.setName(" 希腊披萨 ");
		} else if (orderType.equals("cheese")) {
			pizza = new CheesePizza();
			pizza.setName(" 奶酪披萨 ");
		} else if (orderType.equals("pepper")) {
			pizza = new PepperPizza();
			pizza.setName("胡椒披萨");
		}
		
		return pizza;
	}

}
