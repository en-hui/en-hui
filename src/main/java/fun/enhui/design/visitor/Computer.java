package fun.enhui.design.visitor;

/**
 *
 * @Author 胡恩会
 * @Date 2020/6/26 14:34
 **/
public class Computer {
    IComputerElement cpu = new Cpu();
    IComputerElement memory = new Memory();

    public void accept(IComputerVisitor visitor){
        cpu.accept(visitor);
        memory.accept(visitor);
    }
}
