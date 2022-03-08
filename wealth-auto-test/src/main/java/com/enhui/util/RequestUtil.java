package com.enhui.util;

import com.alibaba.fastjson.JSON;
import com.enhui.model.LoginTokenLocationEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/** 发送http和https请求 */
@Slf4j
public class RequestUtil {

  /** 初始化cookieStore */
  private static CookieStore cookieStore = new BasicCookieStore();

  private static PoolingHttpClientConnectionManager poolCon = null;
  private static final String HTTP = "http";
  private static final String HTTPS = "https";
  private static final int MAX_TOTAL = 200;

  static {
    try {
      // 创建ssl连接，目的网络通道加密传输
      SSLContext sslContext =
          new SSLContextBuilder()
              .loadTrustMaterial(new TrustAllStrategy()) // 信任所有证书
              .build();

      SSLConnectionSocketFactory sslConnectionSocketFactory =
          new SSLConnectionSocketFactory(
              sslContext, new String[] {"SSLv3", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);

      Registry<ConnectionSocketFactory> registry =
          RegistryBuilder.<ConnectionSocketFactory>create()
              .register(HTTP, PlainConnectionSocketFactory.INSTANCE)
              .register(HTTPS, sslConnectionSocketFactory)
              .build();

      // http--ip--tcp--socket
      // https--ssl--ip--tcp--socket

      // 连接池管理连接
      poolCon = new PoolingHttpClientConnectionManager(registry);
      poolCon.setMaxTotal(MAX_TOTAL);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private RequestUtil() {} // 设置为私有，不能实例化

  private static void reqConfig(HttpRequestBase request, String param) {
    // 配置请求header
    if (JsonUtil.isValid(param)) {
      request.setHeader("Content-Type", "application/json");
    } else {
      request.setHeader("Content-Type", "application/x-www-form-urlencoded");
    }
    request.setHeader("User-Agent", "Mozilla/5.0");

    // header中加token
    String loginLocation = LocalCacheUtil.get(LocalCacheUtil.LOGIN_TOKEN_LOCATION);
    if (LoginTokenLocationEnum.HEADER.toString().equals(loginLocation)) {
      String loginKey = LocalCacheUtil.get(LocalCacheUtil.LOGIN_TOKEN_KEY);
      String loginValue = LocalCacheUtil.get(LocalCacheUtil.LOGIN_TOKEN_VALUE);
      request.addHeader(loginKey,loginValue);
    }

    // 配置请求超时
    RequestConfig.Builder builder = RequestConfig.custom();
    builder.setConnectionRequestTimeout(2000);
    RequestConfig config = builder.build();
    // 入参是RequestConfig接口对象
    request.setConfig(config);
  }

  public static String sendGet(String url, String param) throws Exception {
    // 初始化
    CloseableHttpResponse response = null;
    CloseableHttpClient httpclient = null;
    String result = null;

    try {
      // 生成httpclient对象，用来发请求
      httpclient =
          HttpClients.custom()
              .setDefaultCookieStore(cookieStore)
              .setConnectionManager(poolCon)
              .build();

      // 定义get接口
      HttpGet httpGet = new HttpGet(param == null ? url : url + "?" + param);

      // 请求配置相关
      reqConfig(httpGet, param);

      // 发送请求及得到服务器返回值-上转
      response = httpclient.execute(httpGet);
      int statusCode = response.getStatusLine().getStatusCode(); // 存在代码内部转换
      if (statusCode == HttpStatus.SC_OK) { // statusCode == 200 //HttpEntity接口中方法不适用
        // result = EntityUtils.toString(response.getEntity(),"utf-8");
        result =
            JSON.parseObject(EntityUtils.toString(response.getEntity(), "utf-8")).toJSONString();
      } else {
        System.out.println("请求失败,响应状态=" + response.getStatusLine());
        System.out.println(response);
      }
    } finally {
      // 关闭流，释放资源
      response.close();
      // httpclient.close();
    }
    return result;
  }

  public static String sendPost(String url, String param) throws Exception {
    CloseableHttpResponse response = null;
    CloseableHttpClient httpclient = null;
    String result = null;

    try {
      httpclient =
          HttpClients.custom()
              .setDefaultCookieStore(cookieStore)
              .setConnectionManager(poolCon)
              .build();

      // 定义post接口
      HttpPost httpPost = new HttpPost(url);

      // 接口请求绑定请求的参数 --上转
      if (param != null) {
        StringEntity entity = new StringEntity(param, "utf-8");
        httpPost.setEntity(entity);
      }

      // 请求配置相关
      reqConfig(httpPost, param);

      // 发送请求及得到服务器返回值 --上转
      response = httpclient.execute(httpPost);
      int statusCode = response.getStatusLine().getStatusCode();
      if (statusCode == HttpStatus.SC_OK) {
        // result = EntityUtils.toString(response.getEntity(), "utf-8");
        result =
            JSON.parseObject(EntityUtils.toString(response.getEntity(), "utf-8")).toJSONString();
      } else {
        log.error("请求失败，statusCode:{}",statusCode);
      }
    } finally {
      // 关闭流，释放资源
      response.close();
      // httpclient.close();//用了连接池不关闭
    }
    return result;
  }


  public static void main(String[] args) throws UnsupportedEncodingException {
    String encode = URLEncoder.encode("DP_6_.dp_test.table_lk","utf-8");
    String encode1 = URLEncoder.encode("v2_dptask_1.dp_test.heh_test_date.22","utf-8");
    System.out.println(encode);
    System.out.println(encode1);

    System.out.println("DP_6_.dp_test.table_lk".equals("DP_6_.dp_test.table_lk"));
  }
}
