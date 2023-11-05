package com.enhui.algorithm.system.day01;

import com.enhui.algorithm.util.RandomUtil;

import java.util.Arrays;

public class BinarySelect_NearLeft {

    public static void main(String[] args) {
        int testTimes = 10000;
        int maxSize = 1000;
        int maxValue = 1000;
        boolean success = true;
        for (int i = 0; i < testTimes; i++) {
            int[] arr = RandomUtil.generateRandomArray(maxSize, maxValue);
            Arrays.sort(arr);
            int num = (int) ((maxValue + 1) * Math.random());
            // 有序数组已准备好
            int ans1 = nearLeft(arr, num);
            int ans2 = check(arr, num);
            if (ans1 != ans2) {
                success = false;
                System.out.printf("测试失败，失败数据：「%s」，「%s」", Arrays.toString(arr), num);
                break;
            }
        }
        if (success) {
            System.out.printf("算法正确，测试次数：「%s」", testTimes);
        }
    }

    /**
     * 有序数组中，查找>=某个数字最左侧的位置
     * 例如：1 3 3 4 4 5 7 9 找 4
     *
     * @return
     */
    public static int nearLeft(int[] arr, int num) {
        int index = -1;
        if (arr == null || arr.length < 1) {
            return index;
        }
        int L = 0;
        int R = arr.length - 1;
        while (L < R) {
            int mid = L + ((R - L) >> 1);
            if (arr[mid] == num) {
                index = mid;
                // 接着往左找
                R = mid - 1;
            } else if (arr[mid] > num) {
                R = mid - 1;
            } else if (arr[mid] < num) {
                L = mid + 1;
            }
        }
        if (arr[L] == num) {
            index = L;
        }
        return index;
    }

    /**
     * 对数器
     *
     * @return
     */
    public static int check(int[] arr, int num) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == num) {
                return i;
            }
        }
        return -1;
    }
}
