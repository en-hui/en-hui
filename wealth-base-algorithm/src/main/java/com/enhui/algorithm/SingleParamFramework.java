package com.enhui.algorithm;

/**
 * 一个参数的算法题<br>
 * 随机数据生成器<br>
 * 题解<br>
 * 对数器
 */
public abstract class SingleParamFramework<P1, R> implements CodeFramework {

  /**
   * 模版方法模式<br>
   * 运行指定次数，根据随机参数比较题解与对数器的结果
   */
  void methodTemplate() {
    int testTimes = getTestTimes();
    boolean success = true;
    for (int i = 0; i < testTimes; i++) {
      P1 param1 = randomParam1();
      R checkResult = check(param1);
      R solutionResult = solution(param1);
      if (compareResult(solutionResult, checkResult)) {
        success = false;
        System.out.printf(
            "测试失败，失败用例：「%s」,对数器结果:「%s」，题解结果:「%s」", param1, checkResult, solutionResult);
        break;
      }
    }
    if (success) {
      System.out.printf("算法正确，测试次数：「%s」", testTimes);
    }
  }

  /**
   * 比较结果
   *
   * @param solutionResult 题解答案
   * @param checkResult 对数器答案
   * @return true：题解在该用例下正确
   */
  protected boolean compareResult(R solutionResult, R checkResult) {
    return solutionResult.equals(checkResult);
  }

  /**
   * 测试运行次数
   *
   * @return 次数
   */
  public int getTestTimes() {
    return 100;
  }

  /**
   * 生成随机参数
   *
   * @return 参数
   */
  public abstract P1 randomParam1();

  /**
   * 最优题解
   *
   * @param param1 参数
   * @return 计算结果
   */
  abstract R solution(P1 param1);

  /**
   * 对数器：一个暴力解法
   *
   * @param param1 参数
   * @return 计算结果
   */
  abstract R check(P1 param1);
}
