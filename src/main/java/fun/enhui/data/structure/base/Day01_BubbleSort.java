package fun.enhui.data.structure.base;

/**
 * 冒泡排序
 *
 * @Author 胡恩会
 * @Date 2020/6/2 13:44
 **/
public class Day01_BubbleSort implements BaseSort {
    @Override
    public void sort(int[] arr) {
        // 0-(n-1) 找最大值 和n-1位置交换
        // 0-(n-2) 找最大值 和n-2位置交换
        // 0-(n-3) 找最大值 和n-3位置交换
        // ...
        // 0-1 找最大值 和1位置交换
        for (int i = arr.length - 1; i > 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                if (arr[i] < arr[j]){
                    swap(arr, i, j);
                }
            }
        }
    }


}
