package com.enhui.design.factory.simplefactory.pizzastroe.pizza;

/**
 * 抽象类 Pizza
 * @author: HuEnhui
 * @date: 2019/12/19 16:45
 */
public abstract class Pizza {
    /**
     * 披萨名字
     */
    protected String name;

    /**
     * 准备原材料
     * @author: HuEnhui
     * @date: 2019/12/19 16:46
     */
    public abstract void prepare();

    public void bake(){
        System.out.println(name + "烘烤中");
    }

    public void cut() {
        System.out.println(name + "切割中");
    }

    public void box() {
        System.out.println(name + "打包中");
    }

    public void setName(String name) {
        this.name = name;
    }
}
