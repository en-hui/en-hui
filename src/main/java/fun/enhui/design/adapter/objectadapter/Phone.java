package fun.enhui.design.adapter.objectadapter;

public class Phone {
    /**
     * 充电
     * @author: HuEnhui
     * @date: 2019/12/23 15:47
     */
    public void charging(Voltage5V voltage5V) {
        if (voltage5V.output5V() == 5) {
            System.out.println("电压为5，充电中");
        }else {
            System.out.println("电压不符，无法充电");
        }
    }
}
