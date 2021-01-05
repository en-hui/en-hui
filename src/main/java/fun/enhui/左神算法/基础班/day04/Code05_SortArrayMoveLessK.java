package fun.enhui.左神算法.基础班.day04;

import java.util.PriorityQueue;

/**
 * 已知一个几乎有序的数组。
 * 几乎有序是指，如果把数组排好顺序的话，每个元素移动的距离一定不超过k，并且k相对于数组长度N来说是比较小的
 * 请选择一个合适的排序策略，对这个数组进行排序
 *
 * @author 胡恩会
 * @date 2021/1/4 0:34
 */
public class Code05_SortArrayMoveLessK {
    public static void main(String[] args) {

    }

    public static void sortArrayMoveLessK(int[] arr,int k) {
        // 默认小根堆
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        int index = 0;
        // 把 0 ~ k的数放进堆
        while (index <= k && index <= arr.length-1) {
            heap.add(arr[index]);
            index++;
        }

        int i = 0;
        // 堆里弹一个最小的，在加一个
        for (; index < arr.length; i++,index++) {
            // 弹出最小的
            heap.add(arr[index]);
            arr[i] = heap.poll();
        }

        while (i < arr.length) {
            // 把堆里剩下的都弹出来
            arr[i++] = heap.poll();
        }
    }

}
