package fun.enhui.data.structure.base;

/**
 * 异或运算
 * int类型数，提取最右侧1
 *
 * @Author 胡恩会
 * @Date 2020/6/7 20:31
 **/
public class Day01_Xor02 {
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
