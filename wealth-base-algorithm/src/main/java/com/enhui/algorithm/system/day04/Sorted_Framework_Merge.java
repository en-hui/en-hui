package com.enhui.algorithm.system.day04;

import com.enhui.algorithm.framework.SortedFramework;

import java.util.Arrays;

public class Sorted_Framework_Merge extends SortedFramework {
    public Sorted_Framework_Merge(int testTimes, int maxSize, int maxValue) {
        super(testTimes, maxSize, maxValue);
    }

    public static void main(String[] args) {
        int testTimes = 10000;
        int maxSize = 100;
        int maxValue = 1000;
        SortedFramework sorted = new Sorted_Framework_Merge(testTimes, maxSize, maxValue);
        sorted.template();
    }

    @Override
    public void sort(int[] arr1) {
        if (arr1 == null || arr1.length < 2) {
            return;
        }
        System.out.println("len:" + arr1.length + ",arr:" + Arrays.toString(arr1));
        // 是否递归
        boolean isRecursion = true;
        if (isRecursion) {
            recursion(arr1, 0, arr1.length - 1);
        } else {
            nonRecursion(arr1);
        }
    }

    public void nonRecursion(int[] arr) {
        // 步长
        Integer len = 1;
        int N = arr.length;
        while (len < N) {
            int L = 0;
            while (L < N) {
                int M = L + len - 1;
                if (M >= N) {
                    // 左边都凑不满
                    break;
                }
                int R = Math.min(M + len, N - 1);
                merge(arr, L, M, R);
                L = R + 1;
            }
            // 防止溢出
            if (len > N / 2) {
                break;
            }
            // 每次*2
            len <<= 1;
        }
    }

    public void recursion(int[] arr, int l, int r) {
        if (l == r) {
            return;
        }
        int mid = l + ((r - l) >> 1);
        recursion(arr, l, mid);
        recursion(arr, mid + 1, r);
        merge(arr, l, mid, r);
    }

    public void merge(int[] arr, int l, int m, int r) {
        int[] res = new int[r - l + 1];
        int i = 0;
        int lIndex = l;
        int rIndex = m + 1;
        while (lIndex <= m && rIndex <= r) {
            res[i++] = arr[lIndex] <= arr[rIndex] ? arr[lIndex++] : arr[rIndex++];
        }
        while (lIndex <= m) {
            res[i++] = arr[lIndex++];
        }
        while (rIndex <= r) {
            res[i++] = arr[rIndex++];
        }
        for (int j = 0; j < res.length; j++) {
            arr[l + j] = res[j];
        }
    }
}
