package com.enhui.algorithm.common;

import java.util.Arrays;
import java.util.List;

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
            int[] arr1 = generateRandomArray(maxSize, maxValue);
            int[] arr2 = Arrays.copyOf(arr1, arr1.length);

            sort(arr1);
            Arrays.sort(arr2);

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
     * 交换数组中的两个数的位置
     *
     * @param arr 数组
     * @param i   位置1
     * @param j   位置2
     */
    public void swap(int[] arr, int i, int j) {
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
     * 随机样本生成器
     *
     * @param maxSize  数组最大长度
     * @param maxValue 数组中的最大值
     * @return 随机长度且内容随机的数组
     */
    public int[] generateRandomArray(int maxSize, int maxValue) {
        // 随机长度
        int len = (int) (maxSize * Math.random());
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (maxValue * Math.random()) - (int) (maxValue * Math.random());
        }
        return arr;
    }

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
