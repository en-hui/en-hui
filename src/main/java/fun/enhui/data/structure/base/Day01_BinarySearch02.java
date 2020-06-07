package fun.enhui.data.structure.base;

import fun.enhui.data.structure.utils.ArrayUtil;

/**
 * 二分查找
 * 有序数组中，找>=某个数最左侧的位置
 *
 * @Author 胡恩会
 * @Date 2020/6/6 12:54
 **/
public class Day01_BinarySearch02 {

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
        while (L <= R) {
            mid = R + ((L - R) >> 1);
            if (sortedArr[mid] >= random) {
                index = mid;
                R = mid - 1;
            } else {
                L = mid + 1;
            }
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
        for (int i = 0; i < sortedArr.length; i++) {
            if (sortedArr[i] >= random) {
                index = i;
                break;
            }
        }
        return index;
    }
}
