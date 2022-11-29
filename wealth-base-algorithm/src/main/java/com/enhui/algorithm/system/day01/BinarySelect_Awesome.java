package com.enhui.algorithm.system.day01;

import com.enhui.algorithm.common.RandomUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BinarySelect_Awesome {

    public static void main(String[] args) {
        int testTimes = 10000;
        int maxSize = 1000;
        int maxValue = 1000;
        boolean success = true;
        for (int i = 0; i < testTimes; i++) {
            int[] arr = RandomUtil.generateRandomArray(maxSize, maxValue);
            List<Integer> check = check(arr);
            while (check == null || check.size() == 0) {
                // 没有局部最小，重新生成数组
                arr = RandomUtil.generateRandomArray(maxSize, maxValue);
                check = check(arr);
            }
            int awesome = awesome(arr);
            if (!check.contains(awesome)) {
                success = false;
                System.out.printf("测试失败，失败数据：「%s」,check:「%s」，awesome:「%s」", Arrays.toString(arr), check, awesome);
                break;
            }
        }
        if (success) {
            System.out.printf("算法正确，测试次数：「%s」", testTimes);
        }
    }

    /**
     * 找出局部最小的位置
     *
     * @param arr 一定有局部最小的数组
     * @return
     */
    public static int awesome(int[] arr) {
        if (arr == null || arr.length < 2) {
            return -1;
        }
        if (arr[0] < arr[1]) {
            return 0;
        }
        int len = arr.length - 1;
        if (arr[len - 1] > arr[len]) {
            return len;
        }
        int L = 1;
        int R = len - 1;
        while (L < R) {
            int mid = L + ((R - L) >> 1);
            if (arr[mid] > arr[mid - 1]) {
                R = mid - 1;
            } else if (arr[mid] > arr[mid + 1]) {
                L = mid + 1;
            } else {
                return mid;
            }
        }
        return L;
    }


    /**
     * 遍历找出所有局部最小的数据的位置，没有就是null
     *
     * @param arr 数组
     * @return
     */
    public static List<Integer> check(int[] arr) {
        if (arr == null || arr.length < 2) {
            return null;
        }
        for (int i = 0; i < arr.length - 2; i++) {
            // 相邻两个数字不能相等
            if (arr[i] == arr[i + 1]) {
                return null;
            }
        }
        int len = arr.length - 1;
        List<Integer> ans = new ArrayList<>();
        if (arr[0] < arr[1]) {
            ans.add(0);
        }
        if (arr[len - 1] > arr[len]) {
            ans.add(len);
        }
        for (int i = 1; i <= len - 1; i++) {
            if (arr[i - 1] > arr[i] && arr[i] < arr[i + 1]) {
                ans.add(i);
            }
        }
        if (ans.size() == 0) {
            return null;
        } else {
            return ans;
        }
    }
}
