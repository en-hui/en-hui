package com.enhui.algorithm.左神算法.基础班.day01;

/**
 * 插入排序
 *
 * @author 胡恩会
 * @date 2021/1/1 12:03
 */
public class Code03_InsertSort {

    /**
     * 插入排序：从小到达排序
     * 数据 6 5 4 1 2 3
     * 下标 0 1 2 3 4 5
     **/
    public static void insertSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j > 0; j--) {
                if (arr[j] < arr[j - 1]) {
                    swap(arr, j, j - 1);
                } else {
                    break;
                }
            }

        }
    }

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
