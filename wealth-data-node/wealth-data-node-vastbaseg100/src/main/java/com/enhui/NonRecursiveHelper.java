package com.enhui;

import java.nio.ByteBuffer;

public class NonRecursiveHelper {
  private boolean isContinue;

  private String commitTime;
  private Long startLsn;
  private long lastReceiveLsn;
  private ByteBuffer byteBuffer;

  public boolean isContinue() {
    return isContinue;
  }

  public void setContinue(boolean aContinue) {
    isContinue = aContinue;
  }

  public String getCommitTime() {
    return commitTime;
  }

  public void setCommitTime(String commitTime) {
    this.commitTime = commitTime;
  }

  public Long getStartLsn() {
    return startLsn;
  }

  public void setStartLsn(Long startLsn) {
    this.startLsn = startLsn;
  }

  public long getLastReceiveLsn() {
    return lastReceiveLsn;
  }

  public void setLastReceiveLsn(long lastReceiveLsn) {
    this.lastReceiveLsn = lastReceiveLsn;
  }

  public ByteBuffer getByteBuffer() {
    return byteBuffer;
  }

  public void setByteBuffer(ByteBuffer byteBuffer) {
    this.byteBuffer = byteBuffer;
  }

  public NonRecursiveHelper(
      boolean isContinue,
      String commitTime,
      Long startLsn,
      long lastReceiveLsn,
      ByteBuffer byteBuffer) {
    this.isContinue = isContinue;
    this.commitTime = commitTime;
    this.startLsn = startLsn;
    this.lastReceiveLsn = lastReceiveLsn;
    this.byteBuffer = byteBuffer;
  }
}
