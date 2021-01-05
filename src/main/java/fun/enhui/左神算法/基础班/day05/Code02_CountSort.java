package fun.enhui.左神算法.基础班.day05;

import java.util.Arrays;

/**
 * 计数排序
 *
 * @author 胡恩会
 * @date 2021/1/5 22:15
 */
public class Code02_CountSort {

    public static void main(String[] args) {
        int[] arr = new int[]{28, 36, 42, 24, 23, 22, 29, 79, 103};
        countSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void countSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        // 计数数组
        // 39 在help下标39的位置 个数+1
        int[] help = new int[200];

        for (int i = 0; i < arr.length; i++) {
            help[arr[i]]++;
        }
        // 记最终排序的数组下标
        int index = 0;
        for (int i = 0; i < help.length; i++) {
            while (help[i] != 0) {
                arr[index] = i;
                // 计数减小
                help[i]--;
                index++;
            }
        }
    }
}
