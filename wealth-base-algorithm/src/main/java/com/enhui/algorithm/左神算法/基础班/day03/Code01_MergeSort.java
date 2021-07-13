package com.enhui.algorithm.左神算法.基础班.day03;

import java.util.Arrays;

/**
 * 归并排序
 * 使用递归实现
 *
 * @author 胡恩会
 * @date 2021/1/2 18:06
 */
public class Code01_MergeSort {
    public static void main(String[] args) {
        int[] arr = {1, 2, 5, 4, 6, 4, 9, 7, 8};
        mergeSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }

    public static void mergeSort(int[] arr, int L, int R) {
        // base case
        if (L == R) {
            return;
        }
        int mid = L + ((R - L) >> 2);
        mergeSort(arr, L, mid);
        mergeSort(arr, mid + 1, R);
        merge(arr, L, mid, R);
    }

    /**
     * 将数组中，左右两边分别有序的子数组合并为一个有序的数组
     *
     * @param arr 数组
     * @param L   左边界
     * @param M   中间位置
     * @param R   右边界
     * @return void
     * @author 胡恩会
     * @date 2021/1/2 18:14
     **/
    public static void merge(int[] arr, int L, int M, int R) {
        // 申请同长度数组
        int[] help = new int[R - L + 1];
        int helpIndex = 0;
        int leftIndex = L;
        int rightIndex = M + 1;
        // 只要左边或右边没越界，就一直执行
        while (leftIndex <= M && rightIndex <= R) {
            // 把左子数组和右子数组中小的值放进help数组
            help[helpIndex++] = arr[leftIndex] < arr[rightIndex] ? arr[leftIndex++] : arr[rightIndex++];
        }
        // 只可能有一边越界了，即另一边还有没放完的数
        while (leftIndex <= M) {
            help[helpIndex++] = arr[leftIndex++];
        }
        while (rightIndex <= R) {
            help[helpIndex++] = arr[rightIndex++];
        }

        for (int i = 0; i < help.length; i++) {
            arr[L + i] = help[i];
        }
    }

}
