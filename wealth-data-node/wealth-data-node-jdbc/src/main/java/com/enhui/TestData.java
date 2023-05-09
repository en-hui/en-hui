package com.enhui;

import com.enhui.util.RandomUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Data;

@Data
public class TestData {
  public enum TestType {
    INSERT,
    UPDATE,
    UPSERT,
    DELETE
  }

  public static List<TestData> listData(List<EntityField> fields, int min, int max) {
    List<TestData> list = new ArrayList<>();
    for (int i = min; i < max; i++) {
      for (int j = 0; j < fields.size(); j++) {
        final EntityField entityField = fields.get(j);
        final String name = entityField.getName();
        final String type = entityField.getType();
        if (entityField.isPk()) {
          // 序号做主键
          entityField.setValue(String.valueOf(i));
        } else {
          switch (type) {
            case "STRING":
              final int random = RandomUtil.randomJust(66);
              entityField.setValue(
                  IntStream.range(0, random)
                      .mapToObj(index -> "这是一个字符串-" + index + ".")
                      .collect(Collectors.joining()));
              break;
          }
        }
      }
    }
    return list;
  }
}
