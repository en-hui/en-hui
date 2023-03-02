package com.enhui.algorithm.system.day02;

import com.enhui.algorithm.util.RandomUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Eor_TowNumOddNumberTimes {

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
            int[] ans1 = twoNumOddNumberTimes(arr1);
            int[] ans2 = check(arr2);
            if (!Arrays.equals(arr1, arr2)) {
                success = false;
                System.out.printf("测试失败，失败数据：「%s」,eor:%s,check:%s\n", Arrays.toString(arr1), Arrays.toString(ans1), Arrays.toString(ans2));
                break;
            }
        }
        if (success) {
            System.out.printf("算法正确，测试次数：「%s」\n", testTimes);
        }
    }

    /**
     * 一个数组中有两个数字a和b出现了奇数次，其他数都出现了偶数次，找到并打印这两个数
     *
     * @param arr
     * @return
     */
    public static int[] twoNumOddNumberTimes(int[] arr) {
        int[] ans = new int[2];
        int aEorb = 0;
        for (int i = 0; i < arr.length; i++) {
            aEorb = aEorb ^ arr[i];
        }
        // 提取最右侧的1
        int rightOne = aEorb & (-aEorb);
        int a = 0;
        for (int i = 0; i < arr.length; i++) {
            if ((arr[i] & rightOne) == 0) {
                a = a ^ arr[i];
            }
        }
        ans[0] = a;
        int b = aEorb ^ a;
        ans[1] = b;
        // 排序后比较会一致
        Arrays.sort(ans);
        return ans;
    }

    public static int[] check(int[] arr) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            map.merge(arr[i], 1, Integer::sum);
        }
        int[] ans = new int[2];
        int index = 0;
        for (Integer key : map.keySet()) {
            if (map.get(key) % 2 != 0) {
                ans[index++] = key;
            }
        }
        // 排序后比较会一致
        Arrays.sort(ans);
        return ans;
    }

    /**
     * @param singleMaxCount 奇数次-最大次数
     * @param doubleMaxCount 偶数次-最大次数
     * @param otherMaxCount  其他数个数-最大个数
     * @param maxValue       数组中值-最大值
     * @return
     */
    private static int[] generateRandom(int singleMaxCount, int doubleMaxCount, int otherMaxCount, int maxValue) {
        int otherNumCount = RandomUtil.randomJust(otherMaxCount);
        int singleCount = RandomUtil.randomSingleNum(singleMaxCount);
        int doubleCount = RandomUtil.randomDoubleNum(doubleMaxCount);
        HashSet<Integer> set = new HashSet<>();
        // 两个数字出现了奇数次，其他数都出现了偶数次
        int[] arr = new int[2 * singleCount + doubleCount * otherNumCount];
        int index = 0;
        // 第一个奇次数的数
        int oneNum = RandomUtil.random(maxValue);
        for (int i = 0; i < singleCount; i++) {
            arr[index++] = oneNum;
        }
        set.add(oneNum);
        // 第二个奇次数的数
        int twoNum = RandomUtil.random(maxValue);
        while (set.contains(twoNum)) {
            twoNum = RandomUtil.random(maxValue);
        }
        for (int i = 0; i < singleCount; i++) {
            arr[index++] = twoNum;
        }
        // 偶次数的数
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
