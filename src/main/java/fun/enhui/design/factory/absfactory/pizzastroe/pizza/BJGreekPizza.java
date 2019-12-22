package fun.enhui.design.factory.absfactory.pizzastroe.pizza;

public class BJGreekPizza extends Pizza {
    @Override
    public void prepare() {
        setName("北京的希腊pizza");
        System.out.println(" 北京希腊披萨，准备原材料");
    }
}
