package com.enhui.util;

import java.text.MessageFormat;

public class StringTest {

  public static void main(String[] args) {
    int n = 10000;
    String typeExpression = "int(10)";
    int jdbcType = 102;
    long start1 = System.currentTimeMillis();
    for (int i = 0; i < n; i++) {
      String s1 = String.format("%s:%d", typeExpression, jdbcType);
    }
    long end1 = System.currentTimeMillis();
    for (int i = 0; i < n; i++) {
      String s2 = typeExpression + ":" + jdbcType;
//        String s2 = MessageFormat.format("{0}:{1}", typeExpression, jdbcType);
    }
    long end2 = System.currentTimeMillis();
    System.out.println(" " + (end1 - start1) + "----" + (end2 - end1));
  }
}
