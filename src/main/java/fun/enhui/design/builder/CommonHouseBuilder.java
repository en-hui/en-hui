package fun.enhui.design.builder;

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
