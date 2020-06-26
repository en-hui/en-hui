package fun.enhui.design.visitor;

/**
 * 抽象访问者
 *
 * @Author 胡恩会
 * @Date 2020/6/26 14:35
 **/
public interface IComputerVisitor {
    void visitorCpu(Cpu cpu);
    void visitorMemory(Memory memory);
}
