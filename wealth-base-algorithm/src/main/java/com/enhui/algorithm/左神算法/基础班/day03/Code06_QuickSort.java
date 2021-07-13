package com.enhui.algorithm.左神算法.基础班.day03;

import java.util.Arrays;

/**
 * @author 胡恩会
 * @date 2021/1/2 22:52
 */
public class Code06_QuickSort {
    public static void main(String[] args) {
        int[] arr = {1, 5, 2, 6, 8, 9, 5, 2, 1};
        process3(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }

    public static void process3(int[] arr, int L, int R) {
        if (L >= R) {
            return;
        }
        // 取 L ... R 中随机一个数换到R位置
        swap(arr, L + (int) (Math.random() * (R - L + 1)), R);
        int[] equalArea = netherLandsFlag(arr, L, R);
        process3(arr, 0, equalArea[0] - 1);
        process3(arr, equalArea[1] + 1, R);
    }

    /**
     * 变种荷兰旗
     * 以 arr[R] 为num
     * 将 arr 分为三部分，小于num区域，等于num区域，大于num区域
     * 返回值存储 等于区域的左右下标位置
     **/
    public static int[] netherLandsFlag(int[] arr, int L, int R) {
        if (L > R) {
            return new int[]{-1, -1};
        }
        if (L == R) {
            return new int[]{L, R};
        }
        int num = arr[R];
        // 小于区域 右边界
        int minIndex = L - 1;
        // 大于区域 左边界
        int maxIndex = R + 1;
        for (int i = L; i < R && i < maxIndex; i++) {
            if (arr[i] == num) {

            } else if (arr[i] < num) {
                swap(arr, ++minIndex, i);
            } else {
                // 交换后，让i不变。即此处--，for循环++
                swap(arr, --maxIndex, i--);
            }
        }
        return new int[]{++minIndex, --maxIndex};
    }

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
