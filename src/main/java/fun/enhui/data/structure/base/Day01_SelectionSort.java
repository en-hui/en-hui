package fun.enhui.data.structure.base;

/**
 * 选择排序
 *
 * @Author 胡恩会
 * @Date 2020/6/2 13:09
 **/
public class Day01_SelectionSort implements BaseSort {
    @Override
    public void sort(int[] arr) {
        int minIndex;
        // 0-n 找 minIndex 和0位置交换
        // 1-n 找 minIndex 和1位置交换
        // 2-n 找 minIndex 和2位置交换
        // ...
        // (n-1)-n 找 minIndex 和n-1位置交换
        for (int i = 0; i < arr.length-1; i++) {
            minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                minIndex = arr[minIndex] > arr[j] ? j : minIndex;
            }
            swap(arr, minIndex, i);
        }
    }
}
