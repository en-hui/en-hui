package com.enhui.design.factory.absfactory.pizzastroe.pizza;

public class BJPepperPizza extends Pizza {
	@Override
	public void prepare() {
		setName("北京的胡椒披萨pizza");
		System.out.println(" 北京的胡椒披萨，准备原材料");
	}
}
