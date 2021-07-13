package com.enhui.algorithm.左神算法.基础班.day01;

import java.util.Arrays;

/**
 * 二分 -- 在有序数组中，找到>=某数字的最左位置。
 *
 * @author 胡恩会
 * @date 2021/1/1 13:12
 */
public class Code05_BSNearLeft {

    /**
     *
     **/
    public static int nearestIndex(int[] sortedArr, int num) {
        if (sortedArr == null || sortedArr.length == 0) {
            return -1;
        }
        int count = 0;
        int L = 0;
        int R = sortedArr.length - 1;
        // 记录最终结果。最左侧下标
        int index = -1;
        while (L <= R) {
            count++;
            int mid = L + ((R - L) >> 1);
            if (sortedArr[mid] >= num) {
                index = mid;
                R = mid - 1;
            } else {
                L = mid + 1;
            }
        }
        System.out.println("查找次数：" + count);
        return index;
    }

    public static void main(String[] args) {
        int[] arr = {2, 2, 3, 3, 4, 4, 4, 4, 5, 6, 7};
        int num = 4;
        int index = nearestIndex(arr, num);
        System.out.println("数组：" + Arrays.toString(arr) + "中，>=" + num + "的最左边位置是" + index);
    }
}
