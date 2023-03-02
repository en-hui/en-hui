package com.enhui.algorithm.system.day04;

import com.enhui.algorithm.util.RandomUtil;

import java.util.Arrays;
import java.util.Objects;


/**
 * 数组中有多少个逆序对，逆序对：任意两个数据（不用相邻），右边比左边小
 */
public class SortedMerge_ReversePair {

    public static void main(String[] args) {
        int testTimes = 10000;
        int maxSize = 5;
        int maxValue = 10;
        boolean success = true;
        for (int i = 0; i < testTimes; i++) {
            int[] arr1 = RandomUtil.generateRandomArray(maxSize, maxValue);
            int[] arr2 = Arrays.copyOf(arr1, arr1.length);
            int[] arr = Arrays.copyOf(arr1, arr1.length);

            System.out.println(Arrays.toString(arr1));
            System.out.println(Arrays.toString(arr2));

            int ans1 = reversePair(arr1, 0, arr1.length - 1);
            int ans2 = check(arr2);
            if (!Objects.equals(ans1, ans2)) {
                success = false;
                System.out.printf("算法有误，arr:「%s」,ans1:%s,ans2:%s", Arrays.toString(arr), ans1, ans2);
                break;
            }
            System.out.println("================");
        }
        if (success) {
            System.out.printf("算法正确，测试次数：「%s」", testTimes);
        }
    }

    private static int reversePair(int[] arr1, int L, int R) {
        if (arr1 == null || arr1.length < 2 || L == R) {
            return 0;
        }
        int M = L + ((R - L) >> 1);
        return reversePair(arr1, L, M) +
                reversePair(arr1, M + 1, R) +
                merge(arr1, L, M, R);

    }

    private static int merge(int[] arr, int L, int M, int R) {
        int ans = 0;
        int[] res = new int[R - L + 1];
        int i = res.length - 1;
        int RI = R;
        int LI = M;
        // 找 右边比左边小的
        while (RI >= M + 1 && LI >= L) {
            // 谁大放谁，相等放右边
            ans += arr[LI] > arr[RI] ? RI - M : 0;
            res[i--] = arr[LI] > arr[RI] ? arr[LI--] : arr[RI--];
        }
        while (RI >= M + 1) {
            res[i--] = arr[RI--];
        }
        while (LI >= L) {
            res[i--] = arr[LI--];
        }
        for (int j = 0; j < res.length; j++) {
            arr[L + j] = res[j];
        }
        return ans;
    }

    public static int check(int[] arr) {
        int ans = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = arr.length - 1; j > i; j--) {
                if (arr[i] > arr[j]) {
                    ans++;
                }
            }
        }
        return ans;
    }
}
