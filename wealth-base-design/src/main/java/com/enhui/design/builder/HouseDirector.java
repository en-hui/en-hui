package com.enhui.design.builder;

/**
 *  指挥者-动态指定建楼流程
 * @author: HuEnhui
 * @date: 2019/12/23 11:32
 */
public class HouseDirector {
    HouseBuilder houseBuilder = null;

    public HouseDirector(HouseBuilder houseBuilder){
        this.houseBuilder = houseBuilder;
    }

    public void setHouseBuilder(HouseBuilder houseBuilder){
        this.houseBuilder = houseBuilder;
    }

    /**
     * 指挥者指定建造房子流程
     * @author: HuEnhui
     * @date: 2019/12/23 11:33
     */
    public House constructHouse() {
        houseBuilder.buildBasic();
        houseBuilder.buildWalls();
        houseBuilder.roofed();
        return houseBuilder.buildHouse();
    }
}
