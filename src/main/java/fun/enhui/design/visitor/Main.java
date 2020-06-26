package fun.enhui.design.visitor;

/**
 * @Author 胡恩会
 * @Date 2020/6/26 15:00
 **/
public class Main {
    public static void main(String[] args) {
        Computer newComputer = new Computer();
        newComputer.accept(new PersonVisitor());
    }
}
