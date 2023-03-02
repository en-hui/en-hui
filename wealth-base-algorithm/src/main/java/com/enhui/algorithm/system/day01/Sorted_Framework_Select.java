package com.enhui.algorithm.system.day01;

import com.enhui.algorithm.framework.SortedFramework;

public class Sorted_Framework_Select extends SortedFramework {
    public Sorted_Framework_Select(int testTimes, int maxSize, int maxValue) {
        super(testTimes, maxSize, maxValue);
    }

    public static void main(String[] args) {
        int testTimes = 10000;
        int maxSize = 1000;
        int maxValue = 1000;
        SortedFramework sorted = new Sorted_Framework_Select(testTimes, maxSize, maxValue);
        sorted.template();
    }

    /**
     * 选择排序
     *
     * @param arr1 数组
     */
    @Override
    public void sort(int[] arr1) {
        if (arr1 == null || arr1.length < 2) {
            return;
        }
        for (int i = 0; i <= arr1.length - 1; i++) {
            int min = i;
            // 从 i ~ N-1 找最小值
            for (int j = i + 1; j <= arr1.length - 1; j++) {
                min = arr1[min] < arr1[j] ? min : j;
            }
            // i 与最小值位置交换
            swap(arr1, min, i);
        }
    }
}
