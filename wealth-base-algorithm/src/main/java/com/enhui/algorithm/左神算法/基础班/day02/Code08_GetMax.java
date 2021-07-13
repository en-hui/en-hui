package com.enhui.algorithm.左神算法.基础班.day02;

/**
 * 利用递归，求一个数组中的最大值
 *
 * @author 胡恩会
 * @date 2021/1/2 12:23
 */
public class Code08_GetMax {

    public static void main(String[] args) {
        int[] arr = {1, 2, 9, 5, 8, 3, 4, 0};
        int max = getMax(arr, 0, arr.length - 1);
        System.out.println(max);
    }

    /**
     * 求数组 L 到 R范围的最大值
     *
     * @param arr 数组
     * @param L   最左下标
     * @param R   最右下标
     * @return void
     * @author 胡恩会
     * @date 2021/1/2 12:25
     **/
    public static int getMax(int[] arr, int L, int R) {
        if (L == R) {
            return arr[L];
        }
        int mid = L + ((R - L) >> 1);
        int leftMax = getMax(arr, L, mid);
        int rightMax = getMax(arr, mid + 1, R);

        return Math.max(leftMax, rightMax);
    }
}
