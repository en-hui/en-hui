package fun.enhui.左神算法.基础班.day04;

import java.util.Arrays;

/**
 * 实现一个大根堆
 * 支持添加元素操作，和取出最大值操作
 * 包含 heapInsert heapify
 *
 * @author 胡恩会
 * @date 2021/1/4 0:31
 */
public class Code02_Heap01 {
    public static void main(String[] args) {
        MyHeap heap = new MyHeap();
        for (int i = 0; i < 5; i++) {
            heap.push(i);
        }
        System.out.println(Arrays.toString(heap.heap));
        heap.removeMax();
        System.out.println(Arrays.toString(heap.heap));
    }

    public static class MyHeap {
        private int[] heap;

        // 数组容量
        private int capacity = 10;

        private int heapSize = 0;

        public MyHeap() {
            heap = new int[capacity];
        }

        public void push(int value) {
            if (heapSize == capacity) {
                throw new RuntimeException("堆已经满了");
            }
            heap[heapSize] = value;
            heapInsert(heap, heapSize++);
        }

        /**
         * 堆中新增数据时，将元素上移到对应位置，保持堆结构
         *
         * @param heap  堆
         * @param index 元素下标
         * @return void
         * @author 胡恩会
         * @date 2021/1/4 22:12
         **/
        public void heapInsert(int[] heap, int index) {
            // 当前节点大于子节点交换，不大于时停
            // 0位置的父节点还是0，不大于 也停
            while (heap[index] > heap[(index - 1) / 2]) {
                swap(heap, index, (index - 1) / 2);
                index = (index - 1) / 2;
            }
        }

        /**
         * 移除最大值并返回
         * 保持大根堆结构，将0位置与最后一个位置交换
         * 然后在调整0位置到合适位置
         *
         * @return int
         * @author 胡恩会
         * @date 2021/1/4 22:23
         **/
        public int removeMax() {
            int max = heap[0];
            // 0位置交换到最后，heapSize 堆变小一个距离
            swap(heap, 0, --heapSize);
            // 把0位置调整到合适为止
            heapify(heap, 0, heapSize);
            return max;
        }

        public void heapify(int[] heap, int index, int heapSize) {
            int left = (index << 1) | 1;
            // 有左孩子
            while (left < heapSize) {
                // 左右孩子挑出大的来
                int largest = left + 1 < heapSize && heap[left + 1] > heap[left] ? left + 1 : left;
                if (heap[largest] > heap[index]) {
                    swap(heap, largest, index);
                    index = largest;
                    left = index * 2 + 1;
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
}
