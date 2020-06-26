package fun.enhui.design.visitor;

/**
 * 具体访问者-个人用户
 *
 * @Author 胡恩会
 * @Date 2020/6/26 14:36
 **/
public class PersonVisitor implements IComputerVisitor {
    @Override
    public void visitorCpu(Cpu cpu) {
        System.out.println("个人用户购物cpu，价格打9折");
    }
    @Override
    public void visitorMemory(Memory memory) {
        System.out.println("个人用户购买内存，价格打9.5折");
    }
}
