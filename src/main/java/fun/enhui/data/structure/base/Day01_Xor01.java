package fun.enhui.data.structure.base;

/**
 * 异或运算
 * 一个数组中有一个数出现了奇数次，其他数出现了偶数次，找出这个奇数次的数
 *
 * @Author 胡恩会
 * @Date 2020/6/7 20:22
 **/
public class Day01_Xor01 {
    public static void printOddTimesNum1(int[] arr) {
        int eor = 0;
        // 所有数字异或即可
        for (int i = 0; i < arr.length; i++) {
            eor ^= arr[i];
        }
        System.out.println(eor);
    }

    public static void main(String[] args) {
        int[] arr = {1, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6};
        printOddTimesNum1(arr);
    }
}
