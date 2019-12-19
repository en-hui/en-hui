package fun.enhui.design.factory.simplefactory.pizzastroe.order;

import fun.enhui.design.factory.simplefactory.pizzastroe.pizza.CheesePizza;
import fun.enhui.design.factory.simplefactory.pizzastroe.pizza.GreekPizza;
import fun.enhui.design.factory.simplefactory.pizzastroe.pizza.PepperPizza;
import fun.enhui.design.factory.simplefactory.pizzastroe.pizza.Pizza;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *  下单
 * @author: HuEnhui
 * @date: 2019/12/19 17:04
 */
public class OrderPizza {

	/**
	 * 传统方式获取对象 - 不使用设计模式
	 * @author: HuEnhui
	 * @date: 2019/12/19 17:45
	 */
	public void traditionWay(){
		Pizza pizza = null;
		String orderType; // 订购披萨的类型
		do {
			orderType = getType();
			if (orderType.equals("greek")) {
				pizza = new GreekPizza();
				pizza.setName(" 希腊披萨 ");
			} else if (orderType.equals("cheese")) {
				pizza = new CheesePizza();
				pizza.setName(" 奶酪披萨 ");
			} else if (orderType.equals("pepper")) {
				pizza = new PepperPizza();
				pizza.setName("胡椒披萨");
			} else {
				break;
			}
			//输出pizza 制作过程
			pizza.prepare();
			pizza.bake();
			pizza.cut();
			pizza.box();

		} while (true);
	}

	/**
	 * 使用简单工厂模式获取对象
	 * @author: HuEnhui
	 * @date: 2019/12/19 18:26
	 */
	public void simpleFactory(SimpleFactory simpleFactory) {
		Pizza pizza = null;
		//用户输入的
		String orderType = "";

		do {
			orderType = getType();
			pizza = simpleFactory.createPizza(orderType);

			//订购成功
			if(pizza != null) {
				pizza.prepare();
				pizza.bake();
				pizza.cut();
				pizza.box();
			} else {
				System.out.println(" 订购披萨失败 ");
				break;
			}
		}while(true);
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
