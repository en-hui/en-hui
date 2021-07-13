package com.enhui.design.adapter.classadapter;

/**
 * 适配器类
 *
 * @Author: 胡恩会
 * @Date: 2020/6/26 16:12
 **/
public class VoltageAdapter extends Voltage220V implements Voltage5V {
    @Override
    public int output5V() {
        int srcV = output220V();
        // 转换为 5V
        int dstV = srcV / 40;
        return dstV;
    }
}
