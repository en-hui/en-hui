package com.enhui.util;

public class RandomUtil {
  /**
   * 生成一个正数随机数(0，maxValue]
   *
   * @param maxValue 最大值
   * @return
   */
  public static int randomJust(int maxValue) {
    maxValue += 1;
    int num = (int) (maxValue * Math.random());
    while (num == 0) {
      num = (int) (maxValue * Math.random());
    }
    return num;
  }
}
