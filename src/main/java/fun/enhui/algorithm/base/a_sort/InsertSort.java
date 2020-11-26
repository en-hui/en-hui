package fun.enhui.algorithm.base.a_sort;

import fun.enhui.algorithm.sdk.BaseSort;

/**
 * 插入排序
 * <p>
 * 核心思路：
 * 从位置0看左边，比左边小交换,直到比左边大，停止
 * 从位置1看左边，比左边小交换,直到比左边大，停止
 * 从位置2看左边，比左边小交换,直到比左边大，停止
 * ...
 * 从位置n-1看左边，比左边小交换,直到比左边大，停止
 *
 * @author 胡恩会
 * @date 2020/11/15 23:44
 */
public class InsertSort implements BaseSort {
    @Override
    public void sort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            for (int j = i; j > 0; j--) {
                if (arr[j] < arr[j - 1]) {
                    swap(arr, j, j - 1);
                } else {
                    break;
                }
            }
        }
    }
}
