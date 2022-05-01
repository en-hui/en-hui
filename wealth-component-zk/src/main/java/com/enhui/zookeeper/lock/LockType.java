package com.enhui.zookeeper.lock;

/**
 * 锁的资源类型<br>
 * 理论上一种业务应该使用一种类型
 *
 * @author huenhui
 */
public enum LockType {
  WORK_LOCK("workLock", "模拟业务：只有一个电话，同时只能有一个人电话工作"),
  PLAY_LOCK("playLock", "模拟业务：只有一个电脑，同时只能有一个人玩电脑");

  private final String lockName;
  private final String lockDesc;

  LockType(String lockName, String desc) {
    this.lockName = lockName;
    this.lockDesc = desc;
  }

  public String getLockName() {
    return lockName;
  }

  public String getLockDesc() {
    return lockDesc;
  }
}
