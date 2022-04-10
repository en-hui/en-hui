package com.enhui;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/** 自定义hive函数 */
public class MyFunction extends UDF {

  /** 脱敏函数 */
  public Text evaluate(Text s) {
    if (s == null) {
      return null;
    }
    String str = s.toString().substring(0, 1) + "***";
    return new Text(str);
  }
}
