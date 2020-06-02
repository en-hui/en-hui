package fun.enhui.data.structure.base;

/**
 * 排序接口
 * @Author 胡恩会
 * @Date 2020/6/2 20:42
 **/
public interface BaseSort {
    void sort(int[] arr);

    /**
     * 用异或交换数组中两个值（两个值的内存一样会有问题）
     *
     * @param arr:
     * @param i:
     * @param j:
     * @Author: 胡恩会
     * @Date: 2020/6/2 13:27
     * @return: void
     **/
    default void swap(int[] arr, int i, int j) {
        if (i != j) {
            arr[i] = arr[i] ^ arr[j];
            arr[j] = arr[i] ^ arr[j];
            arr[i] = arr[i] ^ arr[j];
        }
    }
}
