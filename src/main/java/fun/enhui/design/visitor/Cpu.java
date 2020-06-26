package fun.enhui.design.visitor;

/**
 * 具体元素-cpu
 *
 * @Author 胡恩会
 * @Date 2020/6/26 14:43
 **/
public class Cpu extends IComputerElement{
    @Override
    public void accept(IComputerVisitor visitor) {
        visitor.visitorCpu(this);
    }
}
