package fun.enhui.algorithm.base.bit_xor;

/**
 * 异或运算
 * int类型数，提取最右侧1
 *
 * @author 胡恩会
 * @date 2020/11/15 23:47
 */
public class xor_02 {
    public static int findFarRight1(int num) {
        System.out.println(num & ((~num) + 1));
        return num & ((~num) + 1);
    }

    public static void main(String[] args) {
        // 5: 101
        // 6: 110
        findFarRight1(6);
    }
}
