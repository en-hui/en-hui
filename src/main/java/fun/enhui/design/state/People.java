package fun.enhui.design.state;

import lombok.Getter;
import lombok.Setter;

/**
 * 环境类-人
 *
 * @Author 胡恩会
 * @Date 2020/6/27 21:10
 **/
@Getter
@Setter
public class People {
    private MoodState state;
    public People(MoodState state){
        this.state = state;
    }

    public void say(){
        state.say();
    }
}
