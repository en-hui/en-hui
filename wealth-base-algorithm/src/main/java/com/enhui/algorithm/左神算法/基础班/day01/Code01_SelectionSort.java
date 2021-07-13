package com.enhui.algorithm.左神算法.基础班.day01;

/**
 * 选择排序
 *
 * @author 胡恩会
 * @date 2021/1/1 10:36
 */
public class Code01_SelectionSort {

    /**
     * 选择排序
     * 数据 6 5 4 1 2 3
     * 下标 0 1 2 3 4 5
     * 数组长度是N
     * 思路：
     * 0~N-1位置找最小值的位置与0位置交换
     * 1~N-1位置找最小值的位置与1位置交换
     * ...
     * N-2~N-1位置找最小值的位置与N-2位置交换
     **/
    public static void selectionSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        for (int i = 0; i < arr.length - 1; i++) {
            // 默认最小值位置在起始位置
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                minIndex = arr[minIndex] > arr[j] ? j : minIndex;
            }
            swap(arr, minIndex, i);
        }
    }

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

}
