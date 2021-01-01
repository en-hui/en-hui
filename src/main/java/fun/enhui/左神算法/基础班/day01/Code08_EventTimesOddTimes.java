package fun.enhui.左神算法.基础班.day01;

import java.util.Arrays;

/**
 * 异或运算
 * 1.一个数组中有一个数字出现了奇数次，其他数都出现了偶数次，怎么找到并打印这个数？
 * 2.一个数组中有两个数字出现了奇数次，其他数都出现了偶数次，怎么找到并打印这两个数？
 *
 * @author 胡恩会
 * @date 2021/1/1 15:58
 */
public class Code08_EventTimesOddTimes {
    /**
     * arr只有一种数出现奇数次
     * 直接^数组中所有数字
     **/
    public static void printOddTimesNum1(int[] arr) {
        int eor = 0;
        for (int i = 0; i < arr.length; i++) {
            eor ^= arr[i];
        }
        System.out.println("数组：" + Arrays.toString(arr) + "中，出现奇数次的数字是" + eor);
    }

    /**
     * arr有两种数出现奇数次
     * 假设这两个数分别为a,b
     * 定义 eor=a^b --> 数组全都异或起来
     * 找到eor最右侧的1-->rightOne，假设是N位置，这时可以把整个数组分为N位置是0的数和N位置是1的数
     * rigjhtOne & arr[i] != 0 (数组中所有N位置是1的数) 异或结果是a
     * 则 b = a ^ eor
     *
     **/
    public static void printOddTimesNum2(int[] arr) {
        // eor = a^b
        int eor = 0;
        for (int i = 0; i < arr.length; i++) {
            eor ^= arr[i];
        }
        // 提取最右侧的1，假设是第八位
        int rightOne = eor & (~eor + 1);

        // eor'
        int a = 0;
        for (int i = 0; i < arr.length; i++) {
            // 异或所有第八位不是0的数
            if ((arr[i] & rightOne) != 0) {
                a ^= arr[i];
            }
        }
        int b = eor ^ a;

        System.out.println("数组：" + Arrays.toString(arr) + "中，出现奇数次的两个数字分别是" + a + "，" + b);


    }

    public static void main(String[] args) {
        int[] arr1 = {1, 1, 2, 2, 3, 3, 3, 4, 4, 5, 5, 6, 6};
        printOddTimesNum1(arr1);
        int[] arr2 = {1,1,2,2,2,3,3,4,4,5,5,5,6,6};
        printOddTimesNum2(arr2);
    }
}
