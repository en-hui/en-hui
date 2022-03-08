package com.enhui.testng;

import lombok.extern.slf4j.Slf4j;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

@Slf4j
public class RetryAnalyzer implements IRetryAnalyzer {

  private int retryInit = 1, retryMax = 3;

  // true 重试
  // false 不重试
  public boolean retry(ITestResult iTestResult) {
    if (retryInit <= retryMax) {
      log.info(String.format("RetryAnalyzer retry 第%d次重试", retryInit));
      retryInit++;
      return true;
    }
    retryInit = 1;
    log.info("RetryAnalyzer retry end");
    return false;
  }
}
