package fun.enhui.design.observer;

/**
 * 具体观察者
 *
 * @Author 胡恩会
 * @Date 2020/6/18 21:34
 **/
public class DogObserver implements BaseObserver {


    @Override
    public void actionOnEvent(ChildActionEvent event) {
        Object source = event.getSource();
        if (event.childSleep){
            System.out.println(source + "孩子睡了，狗趴在窝里");
        }
        if (event.childCry){
            System.out.println(source + "孩子哭了，狗汪汪叫");
        }
    }
}
