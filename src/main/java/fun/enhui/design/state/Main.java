package fun.enhui.design.state;

/**
 * @Author 胡恩会
 * @Date 2020/6/27 20:24
 **/
public class Main {
    public static void main(String[] args) {
        People people = new People(new HappyState());
        people.say();

        people.setState(new SadState());
        people.say();
    }
}
