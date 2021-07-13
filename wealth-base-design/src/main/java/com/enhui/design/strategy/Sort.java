package com.enhui.design.strategy;

/**
 * 排序
 *
 * @Author 胡恩会
 * @Date 2020/5/24 22:56
 **/
public class Sort<T> {
    /**
     * 根据比较器对数组排序
     *
     * @param arr:数组
     * @param comparator:比较器
     * @Author: 胡恩会
     * @Date: 2020/5/25 22:31
     * @return: void
     **/
    public void sort(T[] arr, Comparator<T> comparator) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minPos = i;
            for (int j = i + 1; j < arr.length; j++) {
                minPos = comparator.compare(arr[j], arr[minPos]) == -1 ? j : minPos;
            }
            swap(arr, i, minPos);
        }
    }

    public void swap(T[] arr, int i, int j) {
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
