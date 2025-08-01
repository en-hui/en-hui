package com.enhui.memory;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.GraphLayout;

public class JolHashMapTest {

  @Getter
  @AllArgsConstructor
  static class Model {
    private Long totalc;
    private Long totalb;
    private Long totalIB;
    private Long totalIC;
    private Long totalUB;
    private Long totalUC;
    private Long totalDC;
    private Long totalDB;
    private Long totalTranC;
  }

  Map<String, Object> originData = new HashMap<>();
  Map<String, Object> newOriginData = new HashMap<>();

  {
    originData.put("idx", System.currentTimeMillis());
    originData.put("size", System.currentTimeMillis());
    originData.put("last", System.currentTimeMillis());
    originData.put("ts_sec", System.currentTimeMillis());
    originData.put("dp_done_timestamp", System.currentTimeMillis());
    originData.put("rollback.timestamp", System.currentTimeMillis());
    originData.put("skip.position", System.currentTimeMillis());
    originData.put("start_time", System.currentTimeMillis());
    originData.put("snapshot", true);
    originData.put("position", true);
    originData.put("lsn", System.currentTimeMillis());
    originData.put("sync_stage", "SNAPSHOTTING");
    Model model =
        new Model(
            System.currentTimeMillis(),
            System.currentTimeMillis(),
            System.currentTimeMillis(),
            System.currentTimeMillis(),
            System.currentTimeMillis(),
            System.currentTimeMillis(),
            System.currentTimeMillis(),
            System.currentTimeMillis(),
            System.currentTimeMillis());
    newOriginData.putAll(originData);
    newOriginData.put("stats", model);
    // ------统计类 start--------
    originData.put("totalc", System.currentTimeMillis());
    originData.put("totalb", System.currentTimeMillis());
    originData.put("total_i_b", System.currentTimeMillis());
    originData.put("total_i_c", System.currentTimeMillis());
    originData.put("total_u_b", System.currentTimeMillis());
    originData.put("total_u_c", System.currentTimeMillis());
    originData.put("total_d_b", System.currentTimeMillis());
    originData.put("total_d_c", System.currentTimeMillis());
    originData.put("total_tran_c", System.currentTimeMillis());
    // ------统计类 end--------
  }

  private Integer index;
  private Long lastSize;

  @Test
  @DisplayName("测试map将一部分元素合并放到一个对象中，内存占用对比能降低多少")
  public void testMap2Model() {
    System.out.println("全用map：" + GraphLayout.parseInstance(originData).totalSize());
    System.out.println("部分map节点合并到一个model：" + GraphLayout.parseInstance(newOriginData).totalSize());
  }

  @Test
  @DisplayName("测试设置初始化容量对内存占用的影响")
  public void testJolHashMap() {
    System.out.println(GraphLayout.parseInstance(new HashMap<>()).totalSize());

    System.out.println(
        GraphLayout.parseInstance(new HashMap<>((int) (1000 / 0.75f + 1))).totalSize());
  }

  @Test
  @DisplayName("测试Map使用对象池的效果（对象池只能保证map不回收，但entry每次都要回收新建，效果不大）")
  public void testJolHashMapElement() {
    System.out.println("=======测试new map========");
    init();
    Map<String, Object> sourceOffset = new HashMap<>();
    testMap(sourceOffset);

    System.out.println("=======测试clear map========");
    init();
    sourceOffset.clear();
    testMap(sourceOffset);
  }

  private void init() {
    index = 0;
    lastSize = 0L;
  }

  private void testMap(Map<String, Object> sourceOffset) {
    int keyTotalSize = 0;
    int valueTotalSize = 0;
    printSizeBeforeAndAfter(sourceOffset, "空map", 0, 0);

    for (Map.Entry<String, Object> entry : originData.entrySet()) {
      sourceOffset.put(entry.getKey(), entry.getValue());
      int keySize = entry.getKey().getBytes().length;
      int valueSize =
          entry.getValue() instanceof Long ? 8 : entry.getValue().toString().getBytes().length;
      keyTotalSize += keySize;
      valueTotalSize += valueSize;
      printSizeBeforeAndAfter(sourceOffset, entry.getKey(), keySize, valueSize);
    }
    System.out.println("keyTotalSize: " + keyTotalSize + ", valueTotalSize: " + valueTotalSize);
  }

  private void printSizeBeforeAndAfter(
      Map<String, Object> sourceOffset, String key, int keySize, int valueSize) {
    Long size = GraphLayout.parseInstance(sourceOffset).totalSize();
    System.out.println(
        (index++)
            + "--map 总内存占用："
            + size
            + ",key: "
            + key
            + ", 相比较put前内存差值："
            + (size - lastSize)
            + ", 结构内存："
            + (size - lastSize - keySize - valueSize)
            + ", key内存: "
            + keySize
            + ", value内存: "
            + valueSize);
    lastSize = size;
  }
}
