package com.enhui.algorithm.system.day02;

import com.enhui.algorithm.util.RandomUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Eor_OneKOtherM {

    public static void main(String[] args) {
        int testTimes = 1000000;
        // M>1,K<M
        int k = RandomUtil.randomJust(2);
        int m = RandomUtil.randomJust(3);
        int otherKinds = 2;
        int maxValue = 10000;
        boolean success = true;
        for (int i = 0; i < testTimes; i++) {
            int[] arr1 = generateRandom(k, m, otherKinds, maxValue);
            int[] arr2 = Arrays.copyOf(arr1, arr1.length);
            int ans1 = oneKOtherM(arr1, k, m);
            int ans2 = check(arr2, k, m);
            if (ans1 != ans2) {
                success = false;
                System.out.printf("测试失败，失败数据：「%s」,eor:%s,check:%s\n", Arrays.toString(arr1), ans1, ans2);
                break;
            }
        }
        if (success) {
            System.out.printf("算法正确，测试次数：「%s」\n", testTimes);
        }
    }

    /**
     * 一个数组中有一种数a出现K次，其他数都出现M次，M>1,K<M.找到出现了K次的数，要求时间复杂度O(n),额外空间复杂度O(1)
     *
     * @return
     */
    public static int oneKOtherM(int[] arr, int k, int m) {
        int[] binary = new int[32];
        for (int i = 0; i < arr.length; i++) {
            // 虽然是双层循环，但内部这个32，常数的，所以时间复杂度还是O(n)
            for (int j = 0; j < binary.length; j++) {
                // j 位置有1
                if (((arr[i] >> j) & 1) == 1) {
                    binary[j]++;
                }
            }
        }
        int ans = 0;
        for (int i = 0; i < binary.length; i++) {
            if (binary[i] % m != 0) {
                // 说明k在i位置是1, 用｜相当于给i位置补1，因为每个位置初始值都是0，且每个位置只会｜一次
                ans = ans | (1 << i);
            }
        }
        return ans;
    }

    public static int check(int[] arr, int k, int m) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            map.merge(arr[i], 1, Integer::sum);
        }
        for (Integer key : map.keySet()) {
            if (map.get(key) == k) {
                return key;
            }
        }
        // 不会走到这
        return -1;
    }

    /**
     * @param k          k次
     * @param m          m次
     * @param otherKinds m有几种数
     * @param maxValue   每个数最大是多少
     * @return
     */
    private static int[] generateRandom(int k, int m, int otherKinds, int maxValue) {
        // M>1,K<M
        int[] arr = new int[k + m * otherKinds];
        int kVal = RandomUtil.random(maxValue);
        int index = 0;
        for (int i = 0; i < k; i++) {
            arr[index++] = kVal;
        }
        HashSet<Integer> set = new HashSet<>();
        set.add(kVal);
        for (int i = 0; i < otherKinds; i++) {
            int mVal = RandomUtil.random(maxValue);
            while (set.contains(mVal)) {
                mVal = RandomUtil.random(maxValue);
            }
            set.add(mVal);
            for (int j = 0; j < m; j++) {
                arr[index++] = mVal;
            }
        }
        return arr;
    }
}
