package com.enhui.algorithm.system.tx01;

import com.enhui.algorithm.framework.SingleParamFramework;
import com.enhui.algorithm.util.RandomUtil;
import java.util.Arrays;

public class TX01_001 extends SingleParamFramework<int[], int[]> {

  public static void main(String[] args) {
    TX01_001 instance = new TX01_001();
    instance.methodTemplate();
  }

  @Override
  protected int[] randomParam1() {
    int maxSize = 20;
    int maxValue = 100;
    return RandomUtil.generateRandomArray(maxSize, maxValue);
  }

  @Override
  protected int[] solution(int[] param1) {
    int[] newList = Arrays.copyOf(param1, param1.length);
    return newList;
  }

  @Override
  protected int[] check(int[] param1) {
    int[] newList = Arrays.copyOf(param1, param1.length);
    Arrays.sort(newList);
    return newList;
  }
}
