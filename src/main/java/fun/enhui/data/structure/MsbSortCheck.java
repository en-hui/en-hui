package fun.enhui.data.structure;

import java.util.Arrays;
import java.util.Random;

/**
 * 排序检查
 * <p>
 * 思路：
 * 1. 产生足够多的随机样本
 * 2. 用确定正确的算法计算样本结果
 * 3. 对比验证算法的正确性
 *
 * @Author 胡恩会
 * @Date 2020/5/18 22:18
 **/
public class MsbSortCheck {
    public static final int NUM = 10000;

    /**
     * 生成随机数组
     *
     * @author: 胡恩会
     * @date: 2020/5/18 22:22
     */
    public static int[] generateRandomArray() {
        Random r = new Random();
        int[] arr = new int[NUM];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = r.nextInt(NUM);
        }
        return arr;
    }

    public static void check() {
        int count = 0;
        while (count < 100) {
            count ++;
            int[] arr = generateRandomArray();
            int[] arr2 = new int[arr.length];
            System.arraycopy(arr, 0, arr2, 0, arr.length);

            Arrays.sort(arr);
            MsbSort_01_SelectionSort.selectionSort(arr2);

            boolean same = true;
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] != arr2[i]) {
                    same = false;
                }
            }
            if (same == false){
                System.out.println("算法有误");
                return ;
            }
        }
    }

    public static void main(String[] args) {
        check();
    }
}
