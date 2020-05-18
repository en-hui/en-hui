package fun.enhui.data.structure;

/**
 * 选择排序
 * <p>
 * 思路：
 * 第一次遍历全部，找出最小值下标，最小值与第一个数字交换
 * 第二次遍历除第一个数外，找出最小值小标，最小值下标与第二个数字交换
 * 第三次遍历除前两个数外，找出最小值下标，最小值下标与第三个数字交换
 * .....
 *
 * @Author 胡恩会
 * @Date 2020/5/18 21:18
 **/
public class MsbSort_01_SelectionSort {
    public static void main(String[] args) {
        int[] arr = {3, 6, 2, 5, 1, 7, 9, 6, 4, 8};
        selectionSort(arr);
        printArr(arr);
    }

    /**
     * 选择排序
     *
     * @author: 胡恩会
     * @date: 2020/5/18 22:17
     */
    public static void selectionSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minPos = i;
            for (int j = i + 1; j < arr.length; j++) {
                minPos = arr[j] < arr[minPos] ? j : minPos;
            }
            swap(arr, i, minPos);
        }
    }

    /**
     * 数组中指定两个下标，交换位置
     *
     * @author: 胡恩会
     * @date: 2020/5/18 21:42
     */
    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * 打印数组
     *
     * @author: 胡恩会
     * @date: 2020/5/18 21:43
     */
    public static void printArr(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }
}
