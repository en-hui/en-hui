package com.enhui.design.factory.absfactory.pizzastroe.pizza;

public class LDGreekPizza extends Pizza {
    @Override
    public void prepare() {
        setName("伦敦的希腊pizza");
        System.out.println(" 伦敦的希腊pizza，准备原材料");
    }
}
