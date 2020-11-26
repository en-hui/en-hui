package fun.enhui.algorithm.sdk;

/**
 * 排序抽象接口
 * 规范：所有排序算法均需要实现此接口，并重写排序方法sort
 *
 * @author 胡恩会
 * @date 2020/6/2 20:42
 **/
public interface BaseSort {

    /**
     * 排序算法
     * 使用不同算法来实现，重写此方法
     *
     * @param arr 要排序的数组
     * @author 胡恩会
     * @date 2020/11/15 20:42
     **/
    void sort(int[] arr);

    /**
     * 将数组中的【位置1】元素和【位置2】元素进行交换
     * 用异或交换数组中两个值（两个值的内存一样会有问题）
     *
     * @param arr 数组
     * @param i   位置1
     * @param j   位置2
     * @author 胡恩会
     * @date 2020/11/15 22:24
     **/
    default void swap(int[] arr, int i, int j) {
        if (i != j) {
            arr[i] = arr[i] ^ arr[j];
            arr[j] = arr[i] ^ arr[j];
            arr[i] = arr[i] ^ arr[j];
        }
    }
}
