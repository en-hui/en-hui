package fun.enhui.左神算法.基础班.day03;

import java.util.Arrays;

/**
 * 荷兰国旗问题，三色排序
 * 问题：给定一个数组arr，和一个整数num。把小于num的数放在数组的左边，等于num的数放数组的中间，大于num的数放数组的右边
 * 思路：定义两个下标，小于区域 minIndex=-1 大于区域 maxIndex=arr.length
 * 数组遍历与num比较
 * 如果数组中的数等于num，数组下标后移
 * 如果数组中的数小于num，将这个数与 minIndex+1 位置交换， minIndex 后移，数组下标也后移
 * 如果数组中的数大于num，将这个数与 maxIndex-1 位置交换， maxIndex 前移，数组下标不动
 *
 * @author 胡恩会
 * @date 2021/1/2 22:20
 */
public class Code05_NetherLandsFlag {

    public static void main(String[] args) {
        int[] arr = {3, 5, 4, 0, 4, 6, 7, 2};
        int num = 4;
        netherLandsFlag(arr, num);
        System.out.println(Arrays.toString(arr));
    }

    public static void netherLandsFlag(int[] arr, int num) {
        int minIndex = -1;
        int maxIndex = arr.length;
        // i < maxIndex --> 当下标超过大于区域边界，就可以停了
        for (int i = 0; i < maxIndex; i++) {
            if (arr[i] == num) {

            } else if (arr[i] < num) {
                minIndex++;
                swap(arr, minIndex, i);
            } else {
                maxIndex--;
                swap(arr, maxIndex, i);
                // 此处i不变 --> 先-1，循环时在+1
                i = i - 1;
            }
        }
    }

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
