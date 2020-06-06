package fun.enhui.data.structure.base;

import fun.enhui.data.structure.utils.ArrayUtil;

/**
 * 二分查找
 * 有序数组中，找<=某个数最右侧的位置
 *
 * @Author 胡恩会
 * @Date 2020/6/6 12:54
 **/
public class Day01_BinarySearch03 {

    public static void main(String[] args) {
        int maxSize = 100;
        int maxValue = 100;
        int testTimes = 500000;
        boolean succed = true;
        for (int i = 0; i < testTimes; i++) {
            int[] arr = ArrayUtil.generateRandomArray(maxSize, maxValue);
            new Day01_InsertSort().sort(arr);
            int random = (int) (Math.random() * maxValue - Math.random() * maxValue);
            int positionByBinary = binarySearch(arr, random);
            int positionByCycle = cycleSearch(arr, random);
            if (positionByBinary != positionByCycle) {
                succed = false;
                ArrayUtil.printArray(arr);
                System.out.println(random);
                System.out.println("循环查找" + positionByCycle);
                System.out.println("二分查找" + positionByBinary);
                break;
            }
        }
        System.out.println(succed ? "Nice!" : "Fucking fucked!");
    }

    /**
     * 二分查找
     *
     * @param sortedArr:
     * @param random:
     * @Author: 胡恩会
     * @Date: 2020/6/6 13:32
     * @return: int
     **/
    public static int binarySearch(int[] sortedArr, int random) {
        if (sortedArr == null || sortedArr.length == 0) {
            return -1;
        }
        int index = -1;
        int L = 0, R = sortedArr.length - 1;
        int mid = 0;
        while (L < R) {
            mid = R + ((L - R) >> 1);
            if (sortedArr[mid] <= random) {
                index = mid;
                L = mid + 1;
            } else {
                R = mid - 1;
            }
        }
        // 当 L == R 时，没有比较，所以最后要比较一次
        // 极端右边情况：如果L=n-1 R=n mid=n-1.则L mid+1=n，不越界
        // 极端左边情况：如果L=0 R=1 mid=0.则R mid-1=-1，越界了
        if (sortedArr[L] <= random) {
            index = L;
        }
        return index;
    }

    /**
     * 遍历数组查找
     *
     * @param sortedArr:
     * @param random:
     * @Author: 胡恩会
     * @Date: 2020/6/6 13:31
     * @return: int
     **/
    private static int cycleSearch(int[] sortedArr, int random) {
        if (sortedArr == null || sortedArr.length == 0) {
            return -1;
        }
        int index = -1;
        for (int i = sortedArr.length - 1; i >= 0; i--) {
            if (sortedArr[i] <= random) {
                index = i;
                break;
            }
        }
        return index;
    }
}
