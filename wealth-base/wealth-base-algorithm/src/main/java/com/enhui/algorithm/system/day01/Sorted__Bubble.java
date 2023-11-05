package com.enhui.algorithm.system.day01;

import com.enhui.algorithm.framework.Sorted;

public class Sorted__Bubble extends Sorted {

    public Sorted__Bubble(int testTimes, int maxSize, int maxValue) {
        super(testTimes, maxSize, maxValue);
    }

    public static void main(String[] args) {
        int testTimes = 10000;
        int maxSize = 1000;
        int maxValue = 1000;
        Sorted sorted = new Sorted__Bubble(testTimes, maxSize, maxValue);
        sorted.template();
    }

    /**
     * 冒泡排序
     *
     * @param arr1 数组
     */
    @Override
    public void sort(int[] arr1) {
        if (arr1 == null || arr1.length < 2) {
            return;
        }
        for (int i = 0; i <= arr1.length - 1; i++) {
            for (int j = 0; j < arr1.length - 1 - i; j++) {
                if (arr1[j] > arr1[j + 1]) {
                    swap(arr1, j, j + 1);
                }
            }
        }

    }
}
