package com.enhui.algorithm.common;

import java.util.Arrays;

public class RandomUtil {

    /**
     * 随机样本生成器
     * 生成数据范围左闭右闭区间 [-maxValue,maxValue]
     *
     * @param maxSize  数组最大长度
     * @param maxValue 数组中的最大值
     * @return 随机长度且内容随机的数组
     */
    public static int[] generateRandomArray(int maxSize, int maxValue) {
        // 随机长度
        maxSize += 1;
        maxValue += 1;
        int len = (int) (maxSize * Math.random());
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (maxValue * Math.random()) - (int) (maxValue * Math.random());
        }
        return arr;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(generateRandomArray(3, 2)));
    }
}
