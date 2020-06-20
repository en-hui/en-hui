package fun.enhui.design.observer;

/**
 * 事件源对象
 *
 * @Author 胡恩会
 * @Date 2020/6/18 21:38
 **/
public class Child extends BaseSource {
    boolean cry;
    boolean sleep ;
    public void isCry() {
        cry = true;
        this.notifyObserver();
    }
    public void isSleep(){
        sleep = true;
        this.notifyObserver();
    }

    @Override
    public void notifyObserver() {
        ChildActionEvent event = new ChildActionEvent(this,this.cry,this.sleep);
        for (BaseObserver observer : observers) {
            observer.actionOnEvent(event);
        }
    }
}
