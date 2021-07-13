package com.enhui.design.factory.absfactory.pizzastroe.pizza;

/**
 * pizza 抽象类
 * @author: HuEnhui
 * @date: 2019/12/20 9:54
 */
public abstract class Pizza {
	/**
	 * pizza 名称
	 */
	protected String name;

	/**
	 * 准备原材料
	 * @author: HuEnhui
	 * @date: 2019/12/20 9:55
	 */
	public abstract void prepare();

	
	public void bake() {
		System.out.println(name + " 烘烤");
	}

	public void cut() {
		System.out.println(name + " 切割");
	}

	public void box() {
		System.out.println(name + " 打包");
	}

	public void setName(String name) {
		this.name = name;
	}
}
