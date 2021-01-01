package fun.enhui.左神算法.基础班.day01;

/**
 * 冒泡排序
 *
 * @author 胡恩会
 * @date 2021/1/1 11:20
 */
public class Code02_BubbleSort {
    /**
     * 冒泡排序
     * 数据 6 5 4 1 2 3
     * 下标 0 1 2 3 4 5
     * 数组长度是N
     * 思路：
     * 0-N-1位置，相邻位置比较，把大的放后面
     * 0-N-2位置，
     * ...
     * 0-1位置
     **/
    public static void bubbleSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        for (int e = arr.length - 1; e > 0; e--) {
            for (int j = 0; j < e; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                }
            }
        }
    }

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
