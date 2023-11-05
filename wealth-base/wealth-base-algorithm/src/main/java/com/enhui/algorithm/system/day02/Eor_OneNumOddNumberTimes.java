package com.enhui.algorithm.system.day02;

import com.enhui.algorithm.util.RandomUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Eor_OneNumOddNumberTimes {

    public static void main(String[] args) {
        int testTimes = 1000000;
        int singleMaxCount = 5;
        int doubleMaxCount = 5;
        int otherMaxCount = 2;
        int maxValue = 10000;
        boolean success = true;
        for (int i = 0; i < testTimes; i++) {
            int[] arr1 = generateRandom(singleMaxCount, doubleMaxCount, otherMaxCount, maxValue);
            int[] arr2 = Arrays.copyOf(arr1, arr1.length);
            int ans1 = oneNumOddNumberTimes(arr1);
            int ans2 = check(arr2);
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
     * 一个数组中有一个数字出现了奇数次，其他数都出现了偶数次，找到并打印这个数
     *
     * @return
     */
    public static int oneNumOddNumberTimes(int[] arr) {
        int eor = 0;
        for (int i = 0; i < arr.length; i++) {
            eor = eor ^ arr[i];
        }
        return eor;

    }

    public static int check(int[] arr) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            map.merge(arr[i], 1, Integer::sum);
        }
        for (Integer key : map.keySet()) {
            if (map.get(key) % 2 != 0) {
                return key;
            }
        }
        // 不会走到这
        return -1;

    }

    /**
     * @param singleMaxCount 奇数次-最大次数
     * @param doubleMaxCount 偶数次-最大次数
     * @param otherMaxCount  其他数个数-最大个数
     * @param maxValue       数组中值-最大值
     * @return
     */
    private static int[] generateRandom(int singleMaxCount, int doubleMaxCount, int otherMaxCount, int maxValue) {
        // 出现偶数次的数
        int oneNum = RandomUtil.random(maxValue);
        int otherNumCount = RandomUtil.randomJust(otherMaxCount);
        int singleCount = RandomUtil.randomSingleNum(singleMaxCount);
        int doubleCount = RandomUtil.randomDoubleNum(doubleMaxCount);
        // 一个数字出现了奇数次，其他数都出现了偶数次
        int[] arr = new int[singleCount + doubleCount * otherNumCount];
        int index = 0;
        for (int i = 0; i < singleCount; i++) {
            arr[index++] = oneNum;
        }
        HashSet<Integer> set = new HashSet<>();
        set.add(oneNum);
        for (int i = 0; i < otherNumCount; i++) {
            int num = RandomUtil.random(maxValue);
            while (set.contains(num)) {
                num = RandomUtil.random(maxValue);
            }
            set.add(num);
            for (int j = 0; j < doubleCount; j++) {
                arr[index++] = num;
            }
        }
        return arr;
    }
}
