package com.enhui.util;

import java.util.HashMap;
import java.util.Map;

public class LocalCacheUtil {

  /** token放的位置 cookie、 */
  public static final String LOGIN_TOKEN_LOCATION = "login_token_location";

  public static final String LOGIN_TOKEN_KEY = "login_token_key";
  public static final String LOGIN_TOKEN_VALUE = "login_token_value";

  private static final Map<String, String> cache = new HashMap<>();

  public static void addLoginToken(String tokenLocation, String tokenKey, String tokenValue) {
    LocalCacheUtil.put(LocalCacheUtil.LOGIN_TOKEN_LOCATION, tokenLocation);
    LocalCacheUtil.put(LocalCacheUtil.LOGIN_TOKEN_KEY, tokenKey);
    LocalCacheUtil.put(LocalCacheUtil.LOGIN_TOKEN_VALUE, tokenValue);
  }
  /** 向缓存添加数据 */
  public static void put(String key, String value) {
    cache.put(key, value);
  }

  /** 获取缓存中的数据 */
  public static String get(String key) {
    return cache.get(key);
  }
}
