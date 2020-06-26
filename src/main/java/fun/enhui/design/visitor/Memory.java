package fun.enhui.design.visitor;

/**
 * 具体元素-内存
 *
 * @Author 胡恩会
 * @Date 2020/6/26 14:43
 **/
public class Memory extends IComputerElement {
    @Override
    public void accept(IComputerVisitor visitor) {
        visitor.visitorMemory(this);
    }
}
