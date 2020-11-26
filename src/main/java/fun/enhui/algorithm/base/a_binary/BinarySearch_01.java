package fun.enhui.algorithm.base.a_binary;

import fun.enhui.algorithm.base.a_sort.SelectionSort;
import fun.enhui.algorithm.sdk.ArrayUtil;
import fun.enhui.algorithm.sdk.BaseSort;

/**
 * 二分查找
 * 有序数组中找一个数是否存在
 *
 * @author 胡恩会
 * @date 2020/11/15 23:49
 */
public class BinarySearch_01 {
    public static void main(String[] args) {
        int maxSize = 100;
        int maxValue = 100;
        int testTimes = 500000;
        boolean succed = true;
        for (int i = 0; i < testTimes; i++) {
            int[] arr = ArrayUtil.generateRandomArray(maxSize, maxValue);
            BaseSort sort = new SelectionSort();
            sort.sort(arr);
            int random = (int) (Math.random() * maxValue - Math.random() * maxValue);
            boolean existByBinary = binarySearch(arr, random);
            boolean existByCycle = cycleSearch(arr, random);
            if (existByBinary != existByCycle) {
                succed = false;
                ArrayUtil.printArray(arr);
                System.out.println(random);
                System.out.println(existByBinary);
                break;
            }
        }
        System.out.println(succed ? "Nice!" : "Fucking fucked!");
    }

    /**
     * 使用二分查找
     *
     * @param sortedArr: 有序数组
     * @param random:    要找的值
     * @Author: 胡恩会
     * @Date: 2020/6/2 21:57
     * @return: boolean
     **/
    public static boolean binarySearch(int[] sortedArr, int random) {
        if (sortedArr == null || sortedArr.length == 0) {
            return false;
        }
        int L = 0;
        int R = sortedArr.length - 1;
        int mid = 0;
        while (L < R) {
            // (L+R)/2 --> L + R/2 - L/2
            mid = L + ((R - L) >> 1);
            if (sortedArr[mid] == random) {
                return true;
            } else if (sortedArr[mid] < random) {
                L = mid + 1;
            } else {
                R = mid - 1;
            }
        }
        // 当 L == R 时，没有比较，所以最后要比较一次
        // 极端右边情况：如果L=n-1 R=n mid=n-1.则L mid+1=n，不越界
        // 极端左边情况：如果L=0 R=1 mid=0.则R mid-1=-1，越界了
        return sortedArr[L] == random;
    }

    /**
     * 遍历数组查找
     *
     * @param arr:
     * @param random:
     * @Author: 胡恩会
     * @Date: 2020/6/2 21:58
     * @return: boolean
     **/
    public static boolean cycleSearch(int[] arr, int random) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == random) {
                return true;
            }
        }
        return false;
    }
}
