package com.enhui.algorithm.system.day02;

import com.enhui.algorithm.util.RandomUtil;

import java.util.Arrays;

public class Eor_Swap {

    public static void main(String[] args) {
        int testTimes = 1000000;
        int size = 2;
        int maxValue = 10000;
        boolean success = true;
        for (int i = 0; i < testTimes; i++) {
            int[] arr1 = generateRandom(size, maxValue);
            int[] arr2 = Arrays.copyOf(arr1, arr1.length);
            eorSwap(arr1);
            check(arr2);
            if (!Arrays.equals(arr1, arr2)) {
                success = false;
                System.out.printf("测试失败，失败数据：「%s」，「%s」\n", Arrays.toString(arr1), Arrays.toString(arr2));
                break;
            }
        }
        if (success) {
            System.out.printf("算法正确，测试次数：「%s」\n", testTimes);
        }

        int[] arr = new int[2];
        arr[0] = 2;
        arr[1] = 2;

        arr[0] = arr[0] ^ arr[1];
        arr[1] = arr[0] ^ arr[1];
        arr[0] = arr[0] ^ arr[1];
        if (arr[0] == 2 && arr[1] == 2) {
            System.out.println("异或「可以」交换两个「值相同」的数字");
        } else {
            System.out.println("异或「不可以」交换两个「值相同」的数字");
        }
        arr[0] = arr[0] ^ arr[0];
        arr[0] = arr[0] ^ arr[0];
        arr[0] = arr[0] ^ arr[0];
        if (arr[0] == 2) {
            System.out.println("异或「可以」交换两个「地址相同」的数字");
        } else {
            System.out.println("异或「不可以」交换两个「地址相同」的数字");
        }
    }

    /**
     * 不借助第三个变量，交换两个变量的值
     */
    public static void eorSwap(int[] arr) {
        arr[0] = arr[0] ^ arr[1];
        arr[1] = arr[0] ^ arr[1];
        arr[0] = arr[0] ^ arr[1];
    }

    /**
     * 常规方式交换两个数的值
     *
     * @param arr
     */
    public static void check(int[] arr) {
        int temp = arr[0];
        arr[0] = arr[1];
        arr[1] = temp;
    }


    /**
     * 生成一个随机数组
     *
     * @param size     数组长度固定为size
     * @param maxValue 数组中最大值不超过maxValue
     * @return
     */
    public static int[] generateRandom(int size, int maxValue) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = RandomUtil.random(maxValue);
        }
        return arr;
    }
}
