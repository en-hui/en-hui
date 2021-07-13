package com.enhui.design.builder;

public class Client {
    public static void main(String[] args) {
        // 盖普通房子
        HouseDirector houseDirector = new HouseDirector(new CommonHouseBuilder());
        House house = houseDirector.constructHouse();
        System.out.println(house);
        // 盖高楼
        houseDirector.setHouseBuilder(new HighBuildingBuilder());
        House house1 = houseDirector.constructHouse();
        System.out.println(house1);
    }
}
