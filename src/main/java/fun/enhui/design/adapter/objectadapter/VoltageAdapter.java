package fun.enhui.design.adapter.objectadapter;

/**
 * 对象适配器
 * @Author: 胡恩会
 * @Date: 2020/6/26 16:16
 **/
public class VoltageAdapter implements Voltage5V {
    Voltage220V voltage220V;
    public VoltageAdapter(Voltage220V voltage220V) {
        this.voltage220V = voltage220V;
    }
    @Override
    public int output5V() {
        int srcV = voltage220V.output220V();
        // 转换为 5V
        int dstV = srcV/40;
        return dstV;
    }
}
