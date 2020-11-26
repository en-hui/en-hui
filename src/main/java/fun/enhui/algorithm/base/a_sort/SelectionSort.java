package fun.enhui.algorithm.base.a_sort;

import fun.enhui.algorithm.sdk.BaseSort;

/**
 * 选择排序
 * <p>
 * 核心思路：
 * 从 0~n-1 位置，找到最小值位置与0位置交换
 * 从 1~n-1 位置，找到最小值位置与1位置交换
 * 从 2~n-1 位置，找到最小值位置与2位置交换
 * ...
 * 从 n-2~n-1位置，找到最小值位置与n-1位置交换
 *
 * @author 胡恩会
 * @date 2020/11/15 21:43
 */
public class SelectionSort implements BaseSort {

    @Override
    public void sort(int[] arr) {
        int minIndex;

        for (int i = 0; i < arr.length - 1; i++) {
            minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                minIndex = arr[minIndex] > arr[j] ? j : minIndex;
            }
            swap(arr, minIndex, i);
        }
    }
}
