package com.enhui.algorithm.msb.novice;

public class Code001_Binary {
  public static void main(String[] args) {
    int num = 1;
    printBinary(num);

    // 第一位是符号位：0是非负，1是负数
    // 后面31位：负数 取反+1   =  正数
    printBinary(1);
    printBinary(-1);
    System.out.printf("求证：正数负数 %s", 1 == (~(-1) + 1));
  }

  public static void printBinary(int num) {
    for (int i = 31; i >= 0; i--) {
      System.out.print(((num & (1 << i)) == 0 ? "0" : "1"));
    }
    System.out.println();
  }
}
