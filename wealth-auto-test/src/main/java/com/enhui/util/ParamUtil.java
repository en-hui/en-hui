package com.enhui.util;

import com.alibaba.fastjson.JSONObject;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class ParamUtil {

  /** 将参数拼接起来。 案例：password=123&user=123 */
  public static String toSplicingStr(JSONObject jsonObject) {
    StringBuilder resultStr = new StringBuilder();
    for (Map.Entry<String, Object> stringObjectEntry : jsonObject.entrySet()) {
      String key = stringObjectEntry.getKey();
      String value = stringObjectEntry.getValue().toString();
      if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)){
        resultStr.append(key).append("=").append(value).append("&");
      }
    }
    // 最后多拼接的一个&去掉
    return resultStr.substring(0, resultStr.length() - 1);
  }

  /** 将参数转为json字符串。案例：{"password":"123","user":"123"} */
  public static String toJsonStr(JSONObject jsonObject) {
    return jsonObject.toJSONString();
  }

  /** 测试主类 */
  public static void main(String[] args) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("user", "123");
    jsonObject.put("password", "123");
    log.info("拼接处理：{}", ParamUtil.toSplicingStr(jsonObject));
    log.info("转为json字符串:{}", ParamUtil.toJsonStr(jsonObject));
  }
}
