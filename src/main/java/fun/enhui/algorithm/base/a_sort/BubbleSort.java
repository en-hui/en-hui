package fun.enhui.algorithm.base.a_sort;

import fun.enhui.algorithm.sdk.BaseSort;

/**
 * 冒泡排序
 * <p>
 * 核心思路：
 * 从 0-(n-1) 位置 找最大值 和n-1位置交换
 * 从 0-(n-2) 位置 找最大值 和n-2位置交换
 * 从 0-(n-3) 位置 找最大值 和n-3位置交换
 * ...
 * 从 0-1     位置 找最大值 和1位置交换
 *
 * @author 胡恩会
 * @date 2020/11/15 23:40
 */
public class BubbleSort implements BaseSort {

    @Override
    public void sort(int[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                if (arr[i] < arr[j]) {
                    swap(arr, i, j);
                }
            }
        }
    }
}
