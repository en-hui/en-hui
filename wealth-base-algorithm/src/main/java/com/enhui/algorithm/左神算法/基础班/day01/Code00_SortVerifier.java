package com.enhui.algorithm.左神算法.基础班.day01;

import java.util.Arrays;

/**
 * 排序的对数器
 *
 * @author 胡恩会
 * @date 2021/1/1 10:59
 */
public class Code00_SortVerifier {

    /**
     * 系统级排序
     * 一个肯定正确的排序算法
     **/
    public static void comparator(int[] arr) {
        Arrays.sort(arr);
    }

    /**
     * 生成随机长度，随机值的数组
     **/
    public static int[] generateRandomArray(int maxSize, int maxValue) {
        int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxValue + 1) * Math.random() - (maxValue + 1) * Math.random());
        }
        return arr;
    }

    /**
     * 复制一个相同的数组
     **/
    public static int[] copyArray(int[] arr) {
        if (arr == null) {
            return null;
        }
        // new 一个相同长度的数组
        int[] res = new int[arr.length];
        // 遍历赋值
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i];
        }
        return res;
    }

    /**
     * 等值判断
     **/
    public static boolean isEqual(int[] arr1, int[] arr2) {
        if ((arr1 == null && arr2 != null) || (arr1 != null && arr2 == null)) {
            return false;
        }
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 打印数组
     **/
    public static void printArray(int[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + "");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int testTimes = 500000;
        int maxSize = 100;
        int maxValue = 100;
        boolean success = true;
        for (int i = 0; i < testTimes; i++) {
            int[] arr1 = generateRandomArray(maxSize,maxValue);
            int[] arr2 = copyArray(arr1);
            //Code01_SelectionSort.selectionSort(arr1);
            // Code02_BubbleSort.bubbleSort(arr1);
            Code03_InsertSort.insertSort(arr1);
            comparator(arr2);
            if (!isEqual(arr1,arr2)) {
                success = false;
                printArray(arr1);
                printArray(arr2);
                break;
            }
        }
        System.out.println(success ? "Nice" : "Error");
    }
}
