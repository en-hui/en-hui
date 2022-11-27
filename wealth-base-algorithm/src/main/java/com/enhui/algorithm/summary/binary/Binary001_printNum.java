package com.enhui.algorithm.summary.binary;

/**
 * 将int数字的二进制打印出来
 * 例如：
 * 1：00000000000000000000000000000001
 *
 */
public class Binary001_printNum {
    public static void main(String[] args) {
        int num = 1;
        printBinary(num);

        // 第一位是符号位：0是非负，1是负数
        // 后面31位：负数 取反+1   =  正数
        printBinary(1<<31);
        printBinary(-1);
        System.out.printf("求证：正数负数 %s", 1 == (~(-1) + 1));
    }

    public static void printBinary(int num) {
        // int 是32位，即0～31
        for (int i = 31; i >= 0; i--) {
            // 1：00000000000000000000000000000001
            // 1 << n： ...
            // 1 << 31：10000000000000000000000000000000
            System.out.print(((num & (1 << i)) == 0 ? "0" : "1"));
        }
        System.out.println();
    }
}
