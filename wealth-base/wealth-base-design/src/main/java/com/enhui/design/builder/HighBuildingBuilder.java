package com.enhui.design.builder;

/**
 * 具体建造者-高楼建造者
 * @Author: 胡恩会
 * @Date: 2020/6/26 15:30
 **/
public class HighBuildingBuilder extends HouseBuilder {
    @Override
    public void buildBasic() {
        house.setBasic("高楼打地基100米");
    }

    @Override
    public void buildWalls() {
        house.setWall("高楼砌墙20cm");
    }

    @Override
    public void roofed() {
        house.setRoofed("高楼的透明屋顶");
    }
}
