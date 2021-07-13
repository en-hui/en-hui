package com.enhui.algorithm.左神算法.基础班.day03;

/**
 * 求小和问题
 * {1, 3, 4, 2, 5}
 * 一个数组中，所有左侧比自己小的数加起来称为小和
 * 则此数组从1看，没有小和
 * 从3看，有 1
 * 从4看，有 1，3
 * 从2看，有 1
 * 从5看 有 1，3，4，2
 * 则小和为 1*4 + 2*3 + 1*4 + 1*2 = 16
 *
 * @author 胡恩会
 * @date 2021/1/2 19:31
 */
public class Code02_SmallSum {

    public static void main(String[] args) {
        // 1*4 + 2*3 + 1*4 + 1*2
        int[] arr = {1, 3, 4, 2, 5};
        int smallSum = smallSum(arr);
        System.out.println(smallSum);
    }

    public static int smallSum(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        return process(arr, 0, arr.length - 1);
    }

    public static int process(int[] arr, int L, int R) {
        if (L == R) {
            return 0;
        }
        int mid = L + ((R - L) >> 1);
        return process(arr, L, mid) +
                process(arr, mid + 1, R) +
                merge(arr, L, mid, R);
    }

    /**
     * 每次merge的时候
     * 当左边数比右边小，把右边剩余个数*左边数 加到小和里
     * 当左边数和右边一样，把右边放进最终数组
     * 当左边数比右边大，把右边放进最终数组
     **/
    private static int merge(int[] arr, int l, int mid, int r) {
        int[] help = new int[r - l + 1];
        int result = 0;
        int helpIndex = 0;
        int leftIndex = l;
        int rightIndex = mid + 1;
        while (leftIndex <= mid && rightIndex <= r) {
            // 求小和
            result += arr[leftIndex] < arr[rightIndex] ? (r - rightIndex + 1) * arr[leftIndex] : 0;
            // merge
            help[helpIndex++] = arr[leftIndex] < arr[rightIndex] ? arr[leftIndex++] : arr[rightIndex++];
        }

        while (leftIndex <= l) {
            help[helpIndex++] = arr[leftIndex++];
        }
        while (rightIndex <= r) {
            help[helpIndex++] = arr[rightIndex++];
        }

        for (int i = 0; i < help.length; i++) {
            arr[l + i] = help[i];
        }
        return result;
    }
}
