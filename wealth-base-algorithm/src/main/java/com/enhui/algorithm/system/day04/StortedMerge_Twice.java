package com.enhui.algorithm.system.day04;

import com.enhui.algorithm.common.RandomUtil;
import org.junit.Test;

import java.util.Arrays;
import java.util.Objects;

/**
 * 数组中某一位置的num右边有多少个数，乘以2依然比num小，求整个数组中每个数的这种个数的总和
 */
public class StortedMerge_Twice {

    public static void main(String[] args) {
        int testTimes = 10000;
        int maxSize = 10;
        int maxValue = 10;
        boolean success = true;
        for (int i = 0; i < testTimes; i++) {
            int[] arr1 = RandomUtil.generateRandomArray(maxSize, maxValue);
            int[] arr2 = Arrays.copyOf(arr1, arr1.length);
            int[] arr = Arrays.copyOf(arr1, arr1.length);

            int ans1 = twice(arr1, 0, arr1.length - 1);
            int ans2 = check(arr2);
            if (!Objects.equals(ans1, ans2)) {
                success = false;
                System.out.printf("算法有误，arr:「%s」,ans1:%s,ans2:%s", Arrays.toString(arr), ans1, ans2);
                break;
            }
        }
        if (success) {
            System.out.printf("算法正确，测试次数：「%s」", testTimes);
        }
    }

    public static int twice(int[] arr, int L, int R) {
        if (arr == null || arr.length < 2 || L == R) {
            return 0;
        }
        int M = L + ((R - L) >> 1);
        return twice(arr, L, M)
                + twice(arr, M + 1, R)
                + merge(arr, L, M, R);
    }

    private static int merge(int[] arr, int L, int M, int R) {
        int count = 0;
        int LI = L;
        while (LI <= M) {
            int RI = M + 1;
            // 右侧一直尝试右滑
            while (RI <= R && arr[LI] > 2 * arr[RI]) {
                count++;
                RI++;
            }
            // 滑不动了，左边移动，比下一个
            LI++;
        }
        int[] res = new int[R - L + 1];
        int i = 0;
        LI = L;
        int RI = M + 1;
        while (LI <= M && RI <= R) {
            res[i++] = arr[LI] < arr[RI] ? arr[LI++] : arr[RI++];
        }
        while (LI <= M) {
            res[i++] = arr[LI++];
        }
        while (RI <= R) {
            res[i++] = arr[RI++];
        }
        for (int j = 0; j < res.length; j++) {
            arr[L + j] = res[j];
        }
        return count;
    }

    public static int check(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int ans = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > 2 * arr[j]) {
                    ans++;
                }
            }
        }
        return ans;
    }
}
