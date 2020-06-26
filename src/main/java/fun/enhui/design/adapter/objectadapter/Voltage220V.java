package fun.enhui.design.adapter.objectadapter;

/**
 * 适配者
 *
 * @Author: 胡恩会
 * @Date: 2020/6/26 16:17
 **/
public class Voltage220V {
    public int output220V() {
        int src = 220;
        System.out.println("电压= " + src + "伏");
        return src;
    }
}
