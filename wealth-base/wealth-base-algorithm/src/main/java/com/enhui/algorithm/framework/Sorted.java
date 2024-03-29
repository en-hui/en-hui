package com.enhui.algorithm.framework;

import com.enhui.algorithm.util.RandomUtil;

import java.util.Arrays;

/**
 * 排序算法的抽象类-模版方法设计模式
 * <p>
 * 提供了 template，实现 sort 方法后，调用template即可
 */
public abstract class Sorted {
    /**
     * 测试次数
     */
    private int testTimes;
    /**
     * 数组最大size
     */
    private int maxSize;
    /**
     * 数组最大值
     */
    private int maxValue;

    public Sorted(int testTimes, int maxSize, int maxValue) {
        this.testTimes = testTimes;
        this.maxSize = maxSize;
        this.maxValue = maxValue;
    }

    public void template() {
        boolean success = true;
        for (int i = 0; i < testTimes; i++) {
            int[] arr1 = RandomUtil.generateRandomArray(maxSize, maxValue);
            int[] arr2 = Arrays.copyOf(arr1, arr1.length);

            sort(arr1);
            check(arr2);

            if (!compareArr(arr1, arr2)) {
                success = false;
                System.out.printf("算法有误，自定义实现排序为 arr1:「%s」,系统算法排序arr2:「%s」", Arrays.toString(arr1), Arrays.toString(arr2));
                break;
            }
        }
        if (success) {
            System.out.printf("算法正确，测试次数：「%s」", testTimes);
        }
    }

    /**
     * 对数器
     *
     * @param arr2 数组
     */
    private void check(int[] arr2) {
        Arrays.sort(arr2);
    }

    /**
     * 交换数组中的两个数的位置
     *
     * @param arr 数组
     * @param i   位置1
     * @param j   位置2
     */
    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * 自己要实现的排序
     *
     * @param arr1 数组
     */
    public abstract void sort(int[] arr1);

    /**
     * 比较两个数组是否一致
     *
     * @param arr1 数组1
     * @param arr2 数组2
     * @return true表示一致，false表示不一致
     */
    public boolean compareArr(int[] arr1, int[] arr2) {
        return Arrays.equals(arr1, arr2);
    }
}
