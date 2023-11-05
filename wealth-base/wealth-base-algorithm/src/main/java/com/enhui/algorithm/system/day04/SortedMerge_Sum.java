package com.enhui.algorithm.system.day04;

import com.enhui.algorithm.util.RandomUtil;
import org.junit.Test;

import java.util.Arrays;
import java.util.Objects;

/**
 * 小和问题，一个数组，计算每个位置左边比他小的数据的和，要求O(N * logN)
 */
public class SortedMerge_Sum {

    public static void main(String[] args) {
        int testTimes = 10000;
        int maxSize = 100;
        int maxValue = 10;
        boolean success = true;
        for (int i = 0; i < testTimes; i++) {
            int[] arr1 = RandomUtil.generateRandomArray(maxSize, maxValue);
            int[] arr2 = Arrays.copyOf(arr1, arr1.length);
            int[] arr = Arrays.copyOf(arr1, arr1.length);

            System.out.println(Arrays.toString(arr1));
            System.out.println(Arrays.toString(arr2));

            int ans1 = sum(arr1, 0, arr1.length - 1);
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

    public static int sum(int[] arr, int L, int R) {
        if (arr == null || arr.length < 2 || L == R) {
            return 0;
        }
        int M = L + ((R - L) >> 1);
        return sum(arr, L, M)
                + sum(arr, M + 1, R)
                + merge(arr, L, M, R);
    }

    public static int merge(int[] arr, int L, int M, int R) {
        int ans = 0;
        int[] res = new int[R - L + 1];
        int i = 0;
        int LI = L;
        int RI = M + 1;
        while (LI <= M && RI <= R) {
            if (arr[LI] >= arr[RI]) {
                res[i++] = arr[RI++];
            } else {
                // 右边有几个比我大，就加几个我
                int n = R - RI + 1;
                ans += arr[LI] * n;
                res[i++] = arr[LI++];
            }
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
        return ans;
    }

    /**
     * 计算每个位置左边比他小的数据的和
     */
    private static int check(int[] arr1) {
        int ans = 0;
        for (int i = 1; i < arr1.length; i++) {
            for (int j = 0; j < i; j++) {
                ans += arr1[j] < arr1[i] ? arr1[j] : 0;
            }
        }
        return ans;
    }

    @Test
    public void test() {
        int[] arr = {-2, 10, -2, -4};
        System.out.println(sum(arr, 0, arr.length - 1));
    }

}
