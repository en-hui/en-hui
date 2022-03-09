package com.enhui.test;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class ValueTest {
  /** 引用类型测试 */
  @Test
  public void testValue() {
    HashSet<Integer> olds = new HashSet<>();
    olds.add(1);
    olds.add(2);
    ValueModel valueModel = new ValueModel(olds);

    Set<Integer> news = valueModel.getSets();
    news.add(3);
    //        valueModel.setSets(news);

    System.out.println(valueModel.getSets());
  }
}
