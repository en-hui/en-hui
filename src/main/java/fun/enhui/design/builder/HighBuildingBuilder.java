package fun.enhui.design.builder;

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
