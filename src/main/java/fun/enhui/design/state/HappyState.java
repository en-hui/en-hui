package fun.enhui.design.state;

/**
 * 具体状态-开心
 * @Author 胡恩会
 * @Date 2020/6/27 21:09
 **/
public class HappyState extends MoodState{
    @Override
    void say() {
        System.out.println("微笑着说话");
    }
}
