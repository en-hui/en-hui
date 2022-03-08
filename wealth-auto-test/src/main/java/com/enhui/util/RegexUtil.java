package com.enhui.util;

import com.alibaba.fastjson.JSONPath;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

@Slf4j
public class RegexUtil {
  // 初始化定义
  private static String compareRegex = "(\\$\\.\\w+)=([\\w\\u4e00-\\u9fa5]+)";
  private static String depRegex = "[\\w/]+:(\\$[\\.\\w]+)";

  private RegexUtil() {}

  public static void main(String[] args) {
    String expRes = "$.msg=管理员登录成功;$.stateCode=1";
    String actRes = "{\"msg\":\"管理员登录成功\",\"stateCode\":\"1\"}";

    String expRes1 = "$.msg=管理员登录成功;$.stateCode=1;$.age=20;$.name.name=zhangsan;$.name.key=val";
    String actRes1 =
        "{\"msg\":\"管理员登录成功\",\"stateCode\":\"1\",\"age\":\"20\",\"name\":[{\"name\":\"zhangsan\",\"key\":\"val\"}]}";

    compareResult(expRes1, actRes1);
  }

  /**
   * 对于没有层级的数据，可以直接校验
   *
   * <p>例如： expRes = "$.msg=管理员登录成功;$.stateCode=1"; actRes =
   * "{\"msg\":\"管理员登录成功\",\"stateCode\":\"1\"}";
   *
   * <p>有层级的无法正确校验 expRes1 =
   * "$.msg=管理员登录成功;$.stateCode=1;$.age=20;$.name.name=zhangsan;$.name.key=val"; actRes1 =
   * "{\"msg\":\"管理员登录成功\",\"stateCode\":\"1\",\"age\":\"20\",\"name\":[{\"name\":\"zhangsan\",\"key\":\"val\"}]}";
   */
  public static void compareResult(String expRes, String actRes) {
    Pattern p = Pattern.compile(compareRegex);
    Matcher m = p.matcher(expRes);
    while (m.find()) { // 有疑问，需问老师，find匹配方式？
      String jsonPath = m.group(1); // $.msg          $.stateCode
      String expValue = m.group(2); // 管理员登录成功   1
      String actValue = JSONPath.read(actRes, jsonPath).toString();
      System.out.println("预期值=" + expValue + " " + "实际值=" + actValue);
      log.info(
          String.format("RegexUtil compareResult expValue=%s,actValue=%s", expValue, actValue));
      // 断言失败后续程序不在执行
      Assert.assertEquals(expValue, actValue);
    }
  }

  // 对reqData处理
  public static String handlerReqDataOfDep(String reqData) {
    Pattern p = Pattern.compile(depRegex);
    Matcher m = p.matcher(reqData);
    while (m.find()) {
      // 条件为真进入find代码块-有依赖需要后续处理
      String key = m.group(); //    /api/loginCheck:$.msg
      String value = LocalCacheUtil.get(key); // 管理员登录成功

      // 原始字符串- searchword=/api/loginCheck:$.msg
      // 替换字符串- searchword=管理员登录成功
      reqData = reqData.replace(key, value);
      log.info("RegexUtil handlerReqDataOfDep reqData=" + reqData);
      // System.out.println("------test--------" + reqData);
    }
    return reqData;
  }

  // 对excelDepKey处理
  public static void handlerDepKey(String isDep, String excelDepKey, String actResult) {
    if ("YES".equals(isDep)) {
      Pattern p = Pattern.compile(depRegex); // 共用一个正则
      Matcher m = p.matcher(excelDepKey);
      while (m.find()) {
        String depKey = m.group();
        String depValue = JSONPath.read(actResult, m.group(1)).toString();
        LocalCacheUtil.put(depKey, depValue);
        // log.info("RegexUtil handlerDepKey map key = " + depKey + ",value = " + depValue);
        // 上面注释掉代码另一种写法
        log.info(String.format("RegexUtil handlerDepKey map key=%s,value=%s", depKey, depValue));
      }
    }
  }
}
