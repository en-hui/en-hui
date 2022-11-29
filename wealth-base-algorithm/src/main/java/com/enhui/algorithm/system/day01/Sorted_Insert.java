package com.enhui.algorithm.system.day01;

import com.enhui.algorithm.common.Sorted;


public class Sorted_Insert extends Sorted {

    public Sorted_Insert(int testTimes, int maxSize, int maxValue) {
        super(testTimes, maxSize, maxValue);
    }

    public static void main(String[] args) {
        int testTimes = 10000;
        int maxSize = 1000;
        int maxValue = 1000;
        Sorted sorted = new Sorted_Insert(testTimes, maxSize, maxValue);
        sorted.template();
    }

    /**
     * 插入排序
     */
    @Override
    public void sort(int[] arr1) {
        for (int i = 0; i < arr1.length; i++) {
            for (int j = i; j > 0; j--) {
                if (arr1[j] < arr1[j - 1]) {
                    swap(arr1, j, j - 1);
                } else {
                    break;
                }
            }
        }
    }

}
