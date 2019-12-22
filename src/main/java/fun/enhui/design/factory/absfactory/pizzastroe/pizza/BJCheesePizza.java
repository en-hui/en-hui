package fun.enhui.design.factory.absfactory.pizzastroe.pizza;

public class BJCheesePizza extends Pizza {

	@Override
	public void prepare() {
		setName("北京的奶酪pizza");
		System.out.println(" 北京奶酪披萨，准备原材料");
	}

}
