package fun.enhui.design.builder;

/**
 *  抽象建造者-建造房子的抽象类
 * @author: HuEnhui
 * @date: 2019/12/23 11:25
 */
public abstract class HouseBuilder {
    protected House house = new House();

    // 建造房子的流程
    public abstract void buildBasic();
    public abstract void buildWalls();
    public abstract void roofed();

    //
    public House buildHouse() {
        return house;
    }
}
