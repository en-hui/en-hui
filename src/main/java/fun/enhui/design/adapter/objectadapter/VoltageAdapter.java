package fun.enhui.design.adapter.objectadapter;

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
