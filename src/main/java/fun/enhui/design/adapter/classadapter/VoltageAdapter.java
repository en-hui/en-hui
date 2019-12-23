package fun.enhui.design.adapter.classadapter;

public class VoltageAdapter extends Voltage220V implements Voltage5V {
    @Override
    public int output5V() {
        int srcV = output220V();
        // 转换为 5V
        int dstV = srcV/40;
        return dstV;
    }
}
