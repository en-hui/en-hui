package com.enhui.model;

/** 登录后可以获得到token,其他接口在操作时，将token放在哪里 */
public enum LoginTokenLocationEnum {
  /** 放在请求头 */
  HEADER
}
