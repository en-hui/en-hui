# 基于testng的接口自动化工程

> 本工程是基于testng的自动化接口项目    
> testng 官方文档：https://testng.org/doc/documentation-main.html

## 环境准备
- jdk8
- maven依赖管理工具
- idea开发工具

## 使用步骤
1. 在 src/main/resources目录下，创建csv文件
   1. 表头自定义，【建议参数以param.开头，响应以result.开头】。方便从csv中直接看出不同部分含义
   2. 一个csv文件用于一个接口的测试用例数据维护，一行写一个用例数据
2. 在 src/test目录下，创建测试类
   1. 测试类包含一个@DataProvider数据驱动和多个@Test方法
      1. 数据驱动的意义：读取csv文件，将文件内容读取为二维数组返回
      2. test方法
         1. @Test(dataProvider = "loginDataProvider")，括号里写上数据驱动的名称
         2. 方法参数为对应数据驱动csv的每一列，个数必须一致，类型都为String即可
         3. 需要手动将参数添加到一个JSONObject paramJson = new JSONObject();中
         4. 根据请求的类型，选择处理参数的格式
            1. ParamUtil.toJsonStr ：拼成json字符串，适用于post请求
            2. ParamUtil.toSplicingStr ：以 & 拼接，适用于get请求
         5. 发起网络请求后，校验返回结果，解析json比较实际结果于预期结果
3. 创建一个testNg的xml文件，配置要运行的测试类

案例：com.enhui.work下
```csv
CSV.id,Param.username,Param.password,Param.tokenLocation,Param.tokenKey
1,admin,123456,HEADER,Authorization
```
```java
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
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite name="DP测试" parallel="false">

    <test name="接口自动化">
        <classes>
            <class name="com.enhui.work.login.DPLoginTest"/>
<!--            <class name="com.enhui.work.kafka.KafkaListTaskTest"/>-->
<!--            <class name="com.enhui.work.kafka.KafkaListAgentTest"/>-->
            <class name="com.enhui.work.kafka.KafkaConsumerTest"/>
        </classes>
    </test>

</suite>
```
