package fun.enhui.data.structure.base;

import java.util.Arrays;

/**
 * 排序的对数器
 *
 * @Author 胡恩会
 * @Date 2020/6/2 20:39
 **/
public class SortCheck {

    public static void main(String[] args) {
        BaseSort sort = new Day01_InsertSort();

        int testTime = 500000;
        int maxSize = 100;
        int maxValue = 100;
        boolean succed = true;
        for (int i = 0; i < testTime; i++) {
            int arr1[] = generateRandomArray(maxSize, maxValue);
            int arr2[] = copyArray(arr1);
            sort.sort(arr1);
            comparator(arr2);
            if (!isEqual(arr1, arr2)) {
                succed = false;
                printArray(arr1);
                printArray(arr2);
                break;
            }
        }
        System.out.println(succed ? "Nice!" : "Fucking fucked!");


        int arr[] = generateRandomArray(maxSize, maxValue);
        printArray(arr);
        sort.sort(arr);
        printArray(arr);

    }

    /**
     * 打印数组
     *
     * @param arr1:
     * @Author: 胡恩会
     * @Date: 2020/6/2 20:53
     * @return: void
     **/
    private static void printArray(int[] arr1) {
        for (int i = 0; i < arr1.length; i++) {
            System.out.print(arr1[i] + "  ");
        }
        System.out.println();
    }

    /**
     * 判断两个数组是否相同
     *
     * @param arr1:
     * @param arr2:
     * @Author: 胡恩会
     * @Date: 2020/6/2 20:53
     * @return: boolean
     **/
    private static boolean isEqual(int[] arr1, int[] arr2) {
        if ((arr1 == null && arr2 != null) || (arr1 != null && arr2 == null)) {
            return false;
        }
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 使用系统的排序算法——一个肯定正确的算法
     *
     * @param arr2:
     * @Author: 胡恩会
     * @Date: 2020/6/2 13:29
     * @return: void
     **/
    private static void comparator(int[] arr2) {
        Arrays.sort(arr2);
    }

    /**
     * 复制数组
     *
     * @param arr1:原数组
     * @Author: 胡恩会
     * @Date: 2020/6/2 13:26
     * @return: int[] 复制出来的数组
     **/
    private static int[] copyArray(int[] arr1) {
        if (arr1 == null) {
            return null;
        }
        int[] res = new int[arr1.length];
        for (int i = 0; i < arr1.length; i++) {
            res[i] = arr1[i];
        }
        return res;
    }

    /**
     * 随机样本生成器
     *
     * @param maxSize:  最大长度
     * @param maxValue: 最大值
     * @Author: 胡恩会
     * @Date: 2020/6/2 13:22
     * @return: int[]
     **/
    public static int[] generateRandomArray(int maxSize, int maxValue) {
        int arr[] = new int[(int) ((maxSize + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) ((maxValue + 1) * Math.random());
        }
        return arr;
    }
}
