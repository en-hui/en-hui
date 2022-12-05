package com.enhui.algorithm.system.day06;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * 已知一个几乎有序的数组，几乎有序是指，如果把数组排好顺序的话，每个元素移动的距离一定不超过k，并且k相对数组长度是比较小的。选择一个合适的排序策略对数组排序
 */
public class Heap_SortedK {

    public static void main(String[] args) {
        int[] arr = new int[]{2, 1, 4, 3, 6, 5, 8, 7, 10, 9};
        int k = 2;
        sort(arr, k);
        System.out.println(Arrays.toString(arr));
    }

    public static void sort(int[] arr, int k) {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        int size = 0;
        int index = 0;
        int sortedIndex = 0;
        while (index < k) {
            // 先把小根堆塞满
            queue.offer(arr[index++]);
            size++;
        }
        while (index < arr.length && size == k) {
            // 数组中还有数可放，保持放一取一
            arr[sortedIndex++] = queue.poll();
            size--;
            queue.offer(arr[index++]);
            size++;
        }
        while (!queue.isEmpty()) {
            arr[sortedIndex++] = queue.poll();
        }
    }
}
