package com.enhui.work.login;

import com.alibaba.fastjson.JSONObject;
import com.enhui.util.CsvUtil;
import com.enhui.util.LocalCacheUtil;
import com.enhui.util.ParamUtil;
import com.enhui.util.RequestUtil;
import java.io.IOException;
import java.net.InetAddress;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Slf4j
public class DPLoginTest {

  @DataProvider(name = "loginDataProvider")
  public Object[][] dataProvider() throws IOException {
    String loginTestFilePath =
        "/Users/huenhui/IdeaProjects/wealth/wealth-auto-test/src/main/resources/work/login/DPLoginTest.csv";
    Object[][] data = CsvUtil.listData(loginTestFilePath);
    log.info("加载{}文件成功，用例条数为{}", loginTestFilePath, data.length);
    return data;
  }

  @Test(dataProvider = "loginDataProvider")
  public void login(
      String id, String username, String password, String tokenLocation, String tokenKey)
      throws Exception {
    String host = InetAddress.getByName("dev_huenhui").getHostAddress();
    String url = "http://" + host + "/v3/users/login";

    // 请求参数
    JSONObject paramJson = new JSONObject();
    paramJson.put("login", username);
    paramJson.put("password", password);
    // 发起请求
    String result = RequestUtil.sendPost(url, ParamUtil.toJsonStr(paramJson));

    Assert.assertNotNull(result, "result");
    // 返回结果
    JSONObject jsonObject = JSONObject.parseObject(result);
    JSONObject data = (JSONObject) jsonObject.get("data");
    Assert.assertNotNull(data, "data");
    String tokenValue = (String) data.get("token");
    Assert.assertNotNull(tokenValue, "tokenValue");
    LocalCacheUtil.addLoginToken(tokenLocation, tokenKey, tokenValue);
    log.info("登录成功，token存放位置：{}，key:{},value:{}", tokenLocation, tokenKey, tokenValue);
  }
}
