package fun.enhui.data.structure.base;

import fun.enhui.data.structure.utils.ArrayUtil;

/**
 * 二分查找
 * 在一个无序数组中，找出一个局部最小值,返回下标
 *
 * @Author 胡恩会
 * @Date 2020/6/7 19:42
 **/
public class Day01_BinarySearch04 {

    /**
     * 二分查找
     *
     * @param arr:
     * @Author: 胡恩会
     * @Date: 2020/6/7 20:00
     * @return: int
     **/
    public static int binarySearch(int[] arr) {
        if (null == arr || arr.length == 0) {
            return -1;
        }
        if (arr.length == 1 || arr[0] < arr[1]) {
            return 0;
        }
        if (arr[arr.length - 1] < arr[arr.length - 2]) {
            return arr[arr.length - 1];
        }
        int L = 1, R = arr.length - 2;
        int mid = 0;
        while (L < R) {
            mid = R + ((L - R) >> 1);
            if (arr[mid] > arr[mid + 1]) {
                L = mid + 1;
            } else if (arr[mid] > arr[mid - 1]) {
                R = mid - 1;
            } else {
                return mid;
            }
        }
        return L;
    }

    public static void main(String[] args) {
        int maxSize = 10;
        int maxValue = 100;
        int testTimes = 50000;
        for (int i = 0; i < testTimes; i++) {
            int[] arr = ArrayUtil.generateRandomArray(maxSize, maxValue);
            ArrayUtil.printArray(arr);
            int positionByBinary = binarySearch(arr);
            System.out.println("二分查找" + positionByBinary);

        }
    }
}
