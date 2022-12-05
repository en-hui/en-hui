package com.enhui.algorithm.system.day06;

import com.enhui.algorithm.common.Sorted;

/**
 * 堆排序
 */
public class Sorted_Heap extends Sorted {

    public Sorted_Heap(int testTimes, int maxSize, int maxValue) {
        super(testTimes, maxSize, maxValue);
    }

    public static void main(String[] args) {
        int testTimes = 10000;
        int maxSize = 1000;
        int maxValue = 1000;
        Sorted sorted = new Sorted_Heap(testTimes, maxSize, maxValue);
        sorted.template();
    }

    @Override
    public void sort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        // 组织成大根堆结构,两种方式
        // 第一种：O(N*logN) 从上往下 heapInsert
//        for (int i = 0; i < arr.length; i++) {
//            Heap.heapInsert(arr, i);
//        }
        // 第二种：O(N) 从下往上 heapify
        for (int i = arr.length - 1; i >= 0; i--) {
            Heap.heapify(arr, i, arr.length);
        }
        int heapSize = arr.length;
        Sorted.swap(arr,0,--heapSize);
        while (heapSize > 0) {
            Heap.heapify(arr, 0, heapSize);
            swap(arr, 0, --heapSize);
        }
    }

}
