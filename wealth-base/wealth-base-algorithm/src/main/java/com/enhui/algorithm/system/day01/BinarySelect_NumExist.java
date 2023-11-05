package com.enhui.algorithm.system.day01;


import com.enhui.algorithm.util.RandomUtil;

import java.util.Arrays;

public class BinarySelect_NumExist {

    public static void main(String[] args) {
        int testTimes = 10000;
        int maxSize = 1000;
        int maxValue = 1000;
        boolean success = true;
        for (int i = 0; i < testTimes; i++) {
            int[] arr = RandomUtil.generateRandomArray(maxSize, maxValue);
            Arrays.sort(arr);
            int num = (int) (maxValue * Math.random());
            // 有序数组已准备好
            boolean ans1 = numExist(arr, num);
            boolean ans2 = check(arr, num);
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
     * 对数器
     *
     * @param arr 数组
     * @param num 要查找的数
     * @return
     */
    private static boolean check(int[] arr, int num) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == num) {
                return true;
            }
        }
        return false;
    }

    /**
     * 有序数组中，找某个数字是否存在
     * 二分查找
     * 例如：1 3 3 4 4 5 7 9 找 4
     *
     * @param arr 数组
     * @param num 查找数字
     * @return
     */
    public static boolean numExist(int[] arr, int num) {
        if (arr == null || arr.length < 1) {
            return false;
        }
        int L = 0;
        int R = arr.length - 1;
        while (L < R) {
            int mid = L + ((R - L) >> 1);
            if (arr[mid] == num) {
                return true;
            } else if (arr[mid] > num) {
                R = mid - 1;
            } else if (arr[mid] < num) {
                L = mid + 1;
            }
        }
        return arr[L] == num;
    }
}
