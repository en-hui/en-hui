package fun.enhui.data.structure.base;

/**
 * 插入排序
 *
 * @Author 胡恩会
 * @Date 2020/6/2 20:54
 **/
public class Day01_InsertSort implements BaseSort {
    @Override
    public void sort(int[] arr) {
        // 从位置0看左边，比左边小交换,直到比左边大，停止
        // 从位置1看左边，比左边小交换,直到比左边大，停止
        // 从位置2看左边，比左边小交换,直到比左边大，停止
        // ...
        // 从位置n-1看左边，比左边小交换,直到比左边大，停止
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
