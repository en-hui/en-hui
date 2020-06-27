package fun.enhui.design.templatemethod;

/**
 * 抽象类
 * @Author 胡恩会
 * @Date 2020/6/27 16:59
 **/
public abstract class BasePageLoadTemplate {
    public void templateMethod(){
        init();
        loadContent();
    }
    public abstract void init();
    public abstract void loadContent();
}
