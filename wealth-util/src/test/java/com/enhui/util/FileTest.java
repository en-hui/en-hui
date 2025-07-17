package com.enhui.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

public class FileTest {
  String filePath =
      "/Users/huenhui/IdeaProjects/en-hui/wealth-util/src/test/resources/upgrade.json";

  @Test
  public void test2Method() throws IOException {
    testReadFileMeta();
    testReadFile();
  }

  @Test
  public void testReadFile() {
    long start = System.currentTimeMillis();
    Path upgradeRecordFile = Paths.get(filePath);
    if (!Files.exists(upgradeRecordFile)) {
      return;
    }
    UpgradeRecord upgradeRecord;
    try (InputStream is = Files.newInputStream(upgradeRecordFile)) {
      upgradeRecord = JsonUtils.INSTANCE.getObjectMapper().readValue(is, UpgradeRecord.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    for (ConnectorUpgrade upgrade : upgradeRecord.getUpgrades()) {}
    long end = System.currentTimeMillis();
    System.out.println("读取文件并序列化json耗时：" + (end - start));
  }

  /** 3～5毫秒 */
  @Test
  public void testReadFileMeta() throws IOException {
    long start = System.currentTimeMillis();
    Path upgradeRecordFile = Paths.get(filePath);
    if (!Files.exists(upgradeRecordFile)) {
      return;
    }
    FileTime lastModifiedTime = Files.getLastModifiedTime(upgradeRecordFile);
    long millis = lastModifiedTime.toMillis(); // 转换为毫秒时间戳
    if (millis > System.currentTimeMillis()) {}
    long end = System.currentTimeMillis();
    System.out.println("读取文件元数据耗时：" + (end - start));
  }

  @Data
  @NoArgsConstructor
  public static class UpgradeRecord {
    private List<ConnectorUpgrade> upgrades = new ArrayList<>();
    private String lastCheckTimestamp;
  }

  @Data
  @NoArgsConstructor
  public static class ConnectorUpgrade {
    private String connector;
    private Long timestamp;
    private String version;
    private String path;
  }
}
