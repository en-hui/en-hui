package com.enhui.algorithm.左神算法.基础班.day05;

import java.util.Arrays;

/**
 * 基数排序
 * 要求样本是10进制的正整数
 * 经典的基数排序用骚操作,用数组模拟队列入栈出栈过程
 * > 例子：一个数组[100,5,13,27,17,29,19] 进行排序
 * > 所有数字以位数最多的为准，补足位置，前面补0
 * > 做一个0~9的数组，数组上挂着一个队列做桶
 * > 以个位数字比较，放进对应桶中，放完之后在从0号桶开始倒回原数组
 * > 然后以十位数字比较，放进对应桶中，放完之后从0号桶开始在倒回原数组
 * > 以此类推比较到最高位，最后一次放回原数组时，有序了
 *
 * @author 胡恩会
 * @date 2021/1/5 22:16
 */
public class Code03_RadixSort {

    public static void main(String[] args) {
        int[] arr = new int[]{103, 92, 14, 78, 94, 48};
        radixSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 基数排序
     **/
    public static void radixSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        radixSort(arr, 0, arr.length - 1, getMaxBits(arr));
    }

    /**
     * 将数组arr中L...R的数字进行排序
     * 其中最大值的位数为 bitCount
     * 例如{103, 92, 14, 78, 94, 48}最大值是103，位数是3
     **/
    private static void radixSort(int[] arr, int L, int R, int bitCount) {
        // 基数为10
        int radix = 10;
        int i = 0, j = 0;
        int[] help = new int[R - L + 1];
        // 有多少位就进出多少次
        for (int d = 1; d <= bitCount; d++) {
            //10个空间做桶
            // count[0] 当前位（d)是0的数字有多少个
            // count[1] 当前位（d)是1的数字有多少个
            int[] count = new int[radix];
            for (i = L; i <= R; i++) {
                // arr[i] 的 d 位是几   d表示个位、十位、百位...
                j = getDigit(arr[i], d);
                count[j]++;
            }
            // count   变  count'
            // count[0]  当前位（d）是0的数字有多少个
            // count[1]  当前位（d）是0、1的数字有多少个
            for (i = 1; i < radix; i++) {
                count[i] = count[i] + count[i - 1];
            }
            // 数组从右遍历，根据count‘可以得知所属位置
            // 为什么能得知-->假如5位置的个数是6，对应0~5位置，而从右向左找，所以当前数字就在最后，即5位置
            for (i = R; i >= L; i--) {
                j = getDigit(arr[i], d);
                help[count[j] - 1] = arr[i];
                count[j]--;
            }
            for (i = L, j = 0; i <= R; i++, j++) {
                arr[i] = help[j];
            }
        }

    }

    /**
     * value 的 bit 位是几
     * 例如
     * 103   1   就是 3
     * 103   2   就是 0
     * 103   3   就是 1
     **/
    private static int getDigit(int value, int bit) {
        return ((value / ((int) Math.pow(10, bit - 1))) % 10);
    }

    /**
     * 获取最大值的位数
     **/
    public static int getMaxBits(int[] arr) {
        // 初始化一个足够小的数为初始值
        Integer max = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            // 遍历一遍数组找到最大值
            max = Math.max(max, arr[i]);
        }
        int res = 0;
        while (max != 0) {
            // 每次除以10累加一位，可以知道最大值有几位
            max = max / 10;
            res++;
        }
        return res;
    }
}
