package fun.enhui.design.factory.absfactory.pizzastroe.factory;

import fun.enhui.design.factory.absfactory.pizzastroe.pizza.Pizza;

public abstract class AbsFactory {
    /**
     *  符合北京人口味的披萨
     * @author: HuEnhui
     * @date: 2019/12/20 9:32
     */
    public abstract Pizza createBJPizza();

    /**
     * 符合伦敦人口味的披萨
     * @author: HuEnhui
     * @date: 2019/12/22 14:58
     */
    public abstract Pizza createLDPizza();
}
