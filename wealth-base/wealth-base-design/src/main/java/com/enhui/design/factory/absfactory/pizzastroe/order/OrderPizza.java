package com.enhui.design.factory.absfactory.pizzastroe.order;

import com.enhui.design.factory.absfactory.pizzastroe.factory.AbsFactory;
import com.enhui.design.factory.absfactory.pizzastroe.factory.CheesePizzaFactory;
import com.enhui.design.factory.absfactory.pizzastroe.factory.GreekPizzaFactory;
import com.enhui.design.factory.absfactory.pizzastroe.factory.PepperPizzaFactory;
import com.enhui.design.factory.absfactory.pizzastroe.pizza.Pizza;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 抽象工厂模式的下单类
 * @author: HuEnhui
 * @date: 2019/12/20 9:31
 */
public class OrderPizza {

	/**
	 * 根据不同工厂 创建不同类并完成下单
	 * @author: HuEnhui
	 * @date: 2019/12/20 9:33
	 */
	public void orderPizza() {
		AbsFactory factory;
		Pizza pizza = null;
		String localType;
		String orderType;
		do {
			orderType = getOrderType();
			if ("cheese".equals(orderType)){
				factory = new CheesePizzaFactory();
			}else if ("greek".equals(orderType)) {
				factory = new GreekPizzaFactory();
			}else if ("pepper".equals(orderType)) {
				factory = new PepperPizzaFactory();
			}else {
				break;
			}
			localType = getLocalType();
			if ("BJ".equals(localType)){
				pizza = factory.createBJPizza();
			}else if ("LD".equals(localType)) {
				pizza = factory.createLDPizza();
			}

			//订购成功
			if(pizza != null) {
				// 输出pizza制作过程
				pizza.prepare();
				pizza.bake();
				pizza.cut();
				pizza.box();
			} else {
				System.out.println(" 订购披萨失败 ");
				break;
			}

		} while (true);
	}



	/**
	 * 获取客户输入的披萨种类
	 * @author: HuEnhui
	 * @date: 2019/12/20 9:33
	 */
	private String getLocalType() {
		try {
			BufferedReader strin = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("请输入要订购的地区:");
			String str = strin.readLine();
			return str;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * 获取客户输入的披萨种类
	 * @author: HuEnhui
	 * @date: 2019/12/20 9:33
	 */
	private String getOrderType() {
		try {
			BufferedReader strin = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("请输入要订购的pizza种类:");
			String str = strin.readLine();
			return str;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

}
