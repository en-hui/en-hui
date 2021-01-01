package fun.enhui.左神算法.基础班.day01;

import java.util.Arrays;

/**
 * 在无序数组中，找到局部最小位置(无序数组中任意相邻两个数都不相等)
 * 局部最小定义：
 * 如果0位置比1位置小。则0位置算一个局部最小位置
 * 如果N-1位置比N-2位置小，则N位置算一个局部最小位置
 * 如果Mid位置比Mid-1位置小，比Mid+1位置也小，则Mid位置算一个局部最小位置
 *
 * @author 胡恩会
 * @date 2021/1/1 13:41
 */
public class Code07_BSAwesome {

    /**
     * 数据 4 1 2 1 2 1 4 1 2
     * 下标 0 1 2 3 4 5 6 7 8
     * 1.首先排除最左和最右不是局部最小
     * 2.L=1位置，R=7位置，mid=4位置，值是2，比左边大，则R=3位置
     * 3.L=1位置，R=3位置，mid=2位置，值是2，比左边大，则R=1
     * 4.L=1位置，R=1位置，不走循环，直接返回L-->得到1位置
     **/
    public static int getLessIndex(int[] arr) {
        if (arr == null || arr.length == 0) {
            // not exist
            return -1;
        }
        if (arr.length == 1 || arr[0] < arr[1]) {
            return 0;
        }
        if (arr[arr.length - 1] < arr[arr.length - 2]) {
            return arr.length - 1;
        }
        int L = 1;
        int R = arr.length - 2;
        int count = 0;
        while (L < R) {
            count++;
            int mid = L + ((R - L) >> 1);
            if (arr[mid] > arr[mid - 1]) {
                R = mid - 1;
            } else if (arr[mid] > arr[mid + 1]) {
                L = mid + 1;
            } else {
                return mid;
            }
        }
        System.out.println("查找次数：" + count);

        return L;
    }

    public static void main(String[] args) {
        int[] arr = {4, 1, 2, 1, 2, 1, 4, 5};
        int lessIndex = getLessIndex(arr);
        System.out.println("数组：" + Arrays.toString(arr) + "中，局部最小位置是" + lessIndex + "，局部最小值是" + arr[lessIndex]);
    }
}
