package fun.enhui.algorithm.base.bit_xor;

/**
 * 异或运算
 * 一个数组中有两个数出现了奇数次，其他数出现了偶数次，找出这两个奇数次的数
 * 假设a,b出现了奇数次
 * 思路：
 * 1.所有数字异或得到a^b,为eor
 * 2.提取出a^b最右侧（R）的1，为farRight
 * 3.所有R位置是1的数求异或，得到第一个奇数次数字 a。R位置是1的num：eor&num==0
 * 4.eor与第一个数字再次求异或，得到第二个奇数次数字 b。eor^a 就等于 a^b^a
 *
 * @author 胡恩会
 * @date 2020/11/15 23:48
 */
public class xor_03 {
    public static void printOddTimesNum2(int[] arr) {
        int eor = 0;
        // 假设a,b出现奇次，所有的求异或，得到a^b
        for (int i = 0; i < arr.length; i++) {
            eor ^= arr[i];
        }
        // 找a^b最右侧的1.
        int farRight = eor & ((~eor) + 1);
        int onlyOne = 0;
        for (int i = 0; i < arr.length; i++) {
            if ((arr[i] & farRight) == 0) {
                onlyOne ^= arr[i];
            }
        }
        System.out.println(onlyOne);
        System.out.println(onlyOne ^ eor);
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 3, 4, 4, 5, 5, 6, 6};
        printOddTimesNum2(arr);
    }
}
