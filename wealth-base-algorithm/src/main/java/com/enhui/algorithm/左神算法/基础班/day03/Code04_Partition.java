package com.enhui.algorithm.左神算法.基础班.day03;

import java.util.Arrays;

/**
 * Partition 过程
 * 问题：给定一个数组arr，和一个整数num。把小于等于num的数放在数组的左边，大于num的数放数组的右边。
 * 要求额外空间复杂度O(1),时间复杂度O(N)
 * 思路：定义一个 index 指向-1位置
 * 数组遍历与num比较
 * 如果数组中的数小于等于num，将这个数与 index+1位置的数交换，index后移，数组下标也后移
 * 如果数组中的数大于num，直接后移
 *
 * @author 胡恩会
 * @date 2021/1/2 22:03
 */
public class Code04_Partition {

    public static void main(String[] args) {
        int[] arr = {1,3,5,7,9,3,4,6,0};
        int num = 3;
        int index = partition(arr,num);
        System.out.println("partition 后的结果为" + Arrays.toString(arr) + "，以 " + index + " 位置分界");
    }

    public static int partition(int[] arr,int num) {
        int index = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] <= num) {
                swap(arr,++index,i);
            }
        }
        return index;
    }

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
