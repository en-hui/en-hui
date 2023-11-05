package com.enhui.design.builder;

/**
 * 具体建造者-普通房子建造者
 *
 * @Author: 胡恩会
 * @Date: 2020/6/26 15:30
 **/
public class CommonHouseBuilder extends HouseBuilder {
    @Override
    public void buildBasic() {
        house.setBasic("普通房子打地基5米");
    }

    @Override
    public void buildWalls() {
        house.setWall("普通房子砌墙10cm");
    }

    @Override
    public void roofed() {
        house.setRoofed("普通房子屋顶");
    }


}
