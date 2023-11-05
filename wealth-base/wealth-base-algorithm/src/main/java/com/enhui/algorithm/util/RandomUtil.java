package com.enhui.algorithm.util;

import java.util.Arrays;

/**
 * 随机数相关的工具类
 */
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
        int len = randomNonnegative(maxSize);
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = random(maxValue);
        }
        return arr;
    }

    /**
     * 生成一个随机数[-maxValue,maxValue]
     *
     * @param maxValue 最大值
     * @return
     */
    public static int random(int maxValue) {
        maxValue += 1;
        return (int) (maxValue * Math.random()) - (int) (maxValue * Math.random());
    }

    /**
     * 生成一个非负随机数[0，maxValue]
     *
     * @param maxValue 最大值
     * @return
     */
    public static int randomNonnegative(int maxValue) {
        maxValue += 1;
        return (int) (maxValue * Math.random());
    }

    /**
     * 生成一个正数随机数(0，maxValue]
     *
     * @param maxValue 最大值
     * @return
     */
    public static int randomJust(int maxValue) {
        maxValue += 1;
        int num = (int) (maxValue * Math.random());
        while (num == 0) {
            num = (int) (maxValue * Math.random());
        }
        return num;
    }

    /**
     * 生成一个非负随机数[0，maxValue],取奇数，不要0
     *
     * @param maxValue 最大值
     * @return
     */
    public static int randomSingleNum(int maxValue) {
        maxValue += 1;
        int num = (int) (maxValue * Math.random());
        while (num % 2 != 1) {
            num = (int) (maxValue * Math.random());
        }
        return num;
    }

    /**
     * 生成一个非负随机数[0，maxValue],取偶数，不要0
     *
     * @param maxValue 最大值
     * @return
     */
    public static int randomDoubleNum(int maxValue) {
        maxValue += 1;
        int num = (int) (maxValue * Math.random());
        while (num % 2 != 0 || num == 0) {
            num = (int) (maxValue * Math.random());
        }
        return num;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(generateRandomArray(3, 2)));
        System.out.println(randomNonnegative(1));
        System.out.println(random(1));
    }
}
