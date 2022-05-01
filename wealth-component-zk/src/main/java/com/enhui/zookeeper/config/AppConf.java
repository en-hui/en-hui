package com.enhui.zookeeper.config;

import lombok.Data;

/**
 * 配置对象
 *
 * <p>想象成配置中心中的配置项
 *
 * @author huenhui
 */
@Data
public class AppConf {

  /** 是否开启认证 */
  private boolean openAuth;
  /** a服务的地址 */
  private String serviceA;
  /** b服务的地址 */
  private String serviceB;
}
