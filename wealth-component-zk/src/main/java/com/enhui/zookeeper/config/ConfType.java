package com.enhui.zookeeper.config;

public enum ConfType {
  REGISTRY_CONF(RegistryConf.class,"registryConf", "模拟业务：注册配置");

  private final Class confClass;
  private final String confName;
  private final String lockDesc;

  ConfType(Class confClass, String confName, String desc) {
    this.confClass = confClass;
    this.confName = confName;
    this.lockDesc = desc;
  }

    public Class getConfClass() {
        return confClass;
    }

    public String getConfName() {
    return confName;
  }

  public String getLockDesc() {
    return lockDesc;
  }
}
