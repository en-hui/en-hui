package com.enhui.test;

import org.testng.annotations.Test;

public class StrTest {
  @Test
  public void testStr() {
    String str1 = "{12,23,{12,12,23},null,null}";
    String str2 = "{2003,null,null,{1,1003,3},{1,1,5,7}}";
    String str3 =
        "{2003,null,null,{1,1003,1,19,2003,1},{2,4,4,3,10,3,13,5,13,9,11,13,5,13,2,11,2,4,7,5,7,10,10,10,10,5,7,5}}";
    String str4 = "{2002,null,null,{1,4,2,1,2,1,3,2,2},{10,10,10,14,6,10,14,10}}";
    String str = str4;
    Object[] objects = handleSdoGeometryStr(str);
    System.out.println(objects);
  }

  /** {2003,null,null,{1,1003,3},{1,1,5,7}} */
  private static Object[] handleSdoGeometryStr(String str) {
    Object[] attributes = new Object[5];
    // 去掉最外层的 { 和 }
    String substring = str.trim().substring(1, str.length() - 1);
    char[] allChar = substring.toCharArray();
    int position = 0;
    StringBuilder sb = new StringBuilder();
    // 标记，当遇到{ 标记为true
    boolean flag = false;
    for (int i = 0; i < allChar.length; i++) {
      if (allChar[i] != ',' && allChar[i] != '{' && allChar[i] != '}') {
        sb.append(allChar[i]);
      } else if (flag && allChar[i] == ',') {
        // 内部数组中的【,】暂时保留
        sb.append(allChar[i]);
      } else if ((allChar[i] == ',' && allChar[i - 1] != '}') || allChar[i] == '}') {
        String charStr = sb.toString();
        if ("null".equalsIgnoreCase(charStr)) {
          attributes[position] = null;
        } else {
          if (position == 0 || position == 1) {
            attributes[position] = Double.valueOf(charStr);
          } else {
            String[] split = charStr.split(",");
            Double[] splitNum = new Double[split.length];
            for (int i1 = 0; i1 < split.length; i1++) {
              splitNum[i1] = "null".equalsIgnoreCase(split[i1]) ? null : Double.valueOf(split[i1]);
            }
            attributes[position] = splitNum;
          }
        }
        sb = new StringBuilder();
        position++;
        if (flag && allChar[i] == '}') {
          // 离开内部数组，取消标识
          flag = false;
        }
      } else if (allChar[i] == '{') {
        // 进入内部数组，加标识
        flag = true;
      }
    }
    return attributes;
  }
}
