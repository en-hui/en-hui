package com.enhui.algorithm.system.tx17;

public class TX17_002 {
    public static void main(String[] args) {
        int n = 3;
        hanoi(n, "left", "right", "middle");
    }

    /**
     * @param n     结束位置
     * @param from  移动前位置
     * @param to    移动后位置
     * @param other 另一个位置
     */
    public static void hanoi(int n, String from, String to, String other) {
        if (n == 1) {
            System.out.println(formatOutPut(n, from, to));
            return;
        }
        hanoi(n - 1, from, other, to);
        System.out.println(formatOutPut(n, from, to));
        hanoi(n - 1, other, to, from);
    }

    public static String formatOutPut(int data, String from, String to) {
        return String.format("%s 从 %s 移动到了 %s", data, from, to);
    }
}
