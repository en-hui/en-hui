package com.enhui.memory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.GraphLayout;

/** 测试java对象，有属性不初始化，和无属性占用内存是否一致 */
public class JolObjectTest {

  @Test
  public void testJolObject() {
    TestObjectEmpty testObjectEmpty = new TestObjectEmpty();
    print(testObjectEmpty);

    TestObject testObject = null;
    testObject = new TestObject(null, null, null);
    print(testObject);
    testObject = new TestObject(null, new HashMap<>(), null);
    print(testObject);
    testObject = new TestObject(new HashMap<>(), new HashMap<>(), null);
    print(testObject);
    testObject = new TestObject(new HashMap<>(), new HashMap<>(), new HashSet<>());
    print(testObject);
  }

  private void print(Object o) {
    System.out.println("总内存占用" + GraphLayout.parseInstance(o).totalSize());
    System.out.println("内存占用明细：" + GraphLayout.parseInstance(o).toFootprint());
    System.out.println("================");
  }

  @AllArgsConstructor
  private static class TestObject {
    private final Map<String, Object> before;
    private final Map<String, Object> after;
    private final Set<String> unCapturedColumns;
  }

  private static class TestObjectEmpty {}
}
