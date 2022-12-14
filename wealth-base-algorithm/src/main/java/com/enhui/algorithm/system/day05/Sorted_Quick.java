package com.enhui.algorithm.system.day05;

import com.enhui.algorithm.common.Sorted;

public class Sorted_Quick extends Sorted {

    public Sorted_Quick(int testTimes, int maxSize, int maxValue) {
        super(testTimes, maxSize, maxValue);
    }

    public static void main(String[] args) {
        int testTimes = 10000;
        int maxSize = 1000;
        int maxValue = 1000;
        Sorted sorted = new Sorted_Quick(testTimes, maxSize, maxValue);
        sorted.template();
    }

    @Override
    public void sort(int[] arr1) {
        quickSortV3(arr1, 0, arr1.length - 1);
    }

    /**
     * v2版本快排
     */
    public void quickSortV1(int[] arr, int L, int R) {
        if (L >= R) {
            return;
        }
        int mid = netherLandsFlagV1(arr, L, R);
        quickSortV2(arr, L, mid - 1);
        quickSortV2(arr, mid + 1, R);
    }

    /**
     * v2版本快排
     */
    public void quickSortV2(int[] arr, int L, int R) {
        if (L >= R) {
            return;
        }
        int[] pos = netherLandsFlag(arr, L, R);
        quickSortV2(arr, L, pos[0] - 1);
        quickSortV2(arr, pos[1] + 1, R);
    }

    /**
     * v3版本快排
     * 比v2只多一步：随机位置的数与最右侧交换，在用最右侧的数做划分
     */
    public void quickSortV3(int[] arr, int L, int R) {
        if (L >= R) {
            return;
        }
        // 取 L ... R 中随机一个数换到R位置
        swap(arr, L + (int) (Math.random() * (R - L + 1)), R);
        int[] pos = netherLandsFlag(arr, L, R);
        quickSortV2(arr, L, pos[0] - 1);
        quickSortV2(arr, pos[1] + 1, R);
    }

    private int netherLandsFlagV1(int[] arr, int L, int R) {
        if (L > R) {
            return -1;
        }
        if (L == R) {
            return L;
        }
        int num = arr[R];
        int minIndex = L - 1;
        int maxIndex = R;
        int index = L;
        while (index < maxIndex) {
            if (arr[index] <= num) {
                swap(arr, index++, ++minIndex);
            } else if (arr[index] > num) {
                swap(arr, index, --maxIndex);
            }
        }
        swap(arr, maxIndex, R);
        return maxIndex;
    }

    /**
     * 用最右侧的数做划分，
     * 将小于的数放左边，等于的数放中间，大于的数放右边
     * 返回中间区域的坐标
     */
    public int[] netherLandsFlag(int[] arr, int L, int R) {
        if (L > R) {
            return new int[]{-1, -1};
        }
        if (L == R) {
            return new int[]{L, R};
        }
        // 用最右侧的数做划分
        int num = arr[R];
        int index = L;
        int minIndex = L - 1;
        int maxIndex = R + 1;
        while (index < maxIndex) {
            if (arr[index] < num) {
                swap(arr, index++, ++minIndex);
            } else if (arr[index] > num) {
                swap(arr, index, --maxIndex);
            } else {
                index++;
            }
        }
        return new int[]{minIndex + 1, maxIndex - 1};
    }
}
