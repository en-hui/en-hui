package com.enhui.algorithm.左神算法.基础班.day04;

import java.util.Arrays;

/**
 * 堆排序
 *
 * @author 胡恩会
 * @date 2021/1/4 0:31
 */
public class Code04_HeapSort {

    public static void main(String[] args) {
        int[] arr = new int[] {2,4,6,7,9,8,5,1,0};
        heapSort(arr);
        System.out.println(Arrays.toString(arr));
    }
    public static void heapSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        // 组织成大根堆结构
        for (int i = arr.length - 1; i >= 0; i--) {
            heapify(arr,i,arr.length);
        }
        int heapSize = arr.length;
        //
        while (heapSize > 0) {
            swap(arr,0,--heapSize);
            heapify(arr,0,heapSize);
        }
    }

    public static void heapInsert(int[] heap, int index) {
        while (heap[index] > heap[(index - 1) / 2]) {
            swap(heap, index, (index - 1) / 2);
            index = (index - 1) / 2;
        }
    }

    public static void heapify(int[] heap, int index, int heapSize) {
        // 左孩子位置
        int left = 2 * index + 1;
        while (left < heapSize) {
            int largest = left + 1 < heapSize && heap[left + 1] > heap[left] ? left + 1 : left;
            if (heap[index] < heap[largest]) {
                swap(heap, index, largest);
                index = largest;
                left = 2 * index + 1;
            } else {
                break;
            }
        }
    }

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
