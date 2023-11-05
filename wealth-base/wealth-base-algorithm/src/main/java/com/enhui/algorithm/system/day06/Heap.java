package com.enhui.algorithm.system.day06;

import com.enhui.algorithm.util.RandomUtil;
import com.enhui.algorithm.framework.Sorted;

import java.util.Objects;
import java.util.PriorityQueue;

/**
 * 用数组实现大根堆，支持 heapInsert 和 heapify
 */
public class Heap {

    int[] arr;
    int size;
    int heapSize;

    public Heap(int size) {
        this.size = size;
        this.arr = new int[size];
        this.heapSize = 0;
    }

    public static void main(String[] args) {
        int testTimes = 10000;
        int maxSize = 10;
        int maxValue = 10;
        boolean success = true;
        for (int i = 0; i < testTimes; i++) {
            int size = RandomUtil.randomJust(maxSize);
            // 大根堆
            PriorityQueue<Integer> queue = new PriorityQueue<Integer>(size, (a, b) -> b - a);
            Heap heap = new Heap(size);

            for (int j = 0; j < size; j++) {
                int value = RandomUtil.random(maxValue);
                queue.offer(value);
                heap.offer(value);
            }
            for (int j = 0; j < size; j++) {
                Integer poll = queue.poll();
                Integer poll1 = heap.poll();
                if (!Objects.equals(poll, poll1)) {
                    System.out.printf("测试失败，失败数据：size:%s ,%s--%s\n", size, poll, poll1);
                    success = false;
                    break;
                }
            }
            if (!success) {
                break;
            }
        }
        if (success) {
            System.out.printf("算法正确，测试次数：「%s」\n", testTimes);
        }
    }

    private void offer(int value) {
        if (heapSize == size) {
            return;
        }
        arr[heapSize] = value;
        heapInsert(arr, heapSize++);
    }

    private Integer poll() {
        Integer max = null;
        if (heapSize > 0) {
            max = arr[0];
            // 0位置交换到最后，heapSize 堆变小一个距离
            Sorted.swap(arr, 0, --heapSize);
            // 把0位置调整到合适为止
            heapify(arr, 0, heapSize);
        }
        return max;
    }

    public static void heapInsert(int[] heap, int index) {
        // 当前节点大于子节点交换，不大于时停
        // 0位置的父节点还是0，不大于 也停
        while (heap[index] > heap[(index - 1) / 2]) {
            Sorted.swap(heap, index, (index - 1) / 2);
            index = (index - 1) / 2;
        }
    }

    public static void heapify(int[] heap, int index, int heapSize) {
        int left = (index << 1) | 1;
        // 有左孩子
        while (left < heapSize) {
            // 左右孩子挑出大的来
            int largest = left + 1 < heapSize && heap[left + 1] > heap[left] ? left + 1 : left;
            if (heap[largest] > heap[index]) {
                Sorted.swap(heap, largest, index);
                index = largest;
                left = index * 2 + 1;
            } else {
                break;
            }
        }
    }
}
