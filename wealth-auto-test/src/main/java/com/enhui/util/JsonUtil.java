package com.enhui.util;

import com.alibaba.fastjson.JSON;

public class JsonUtil {

  private JsonUtil() {}
  /** 判断jsonstr是否为json */
  public static boolean isValid(String jsonstr) {
    try {
      JSON.parseObject(jsonstr);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
