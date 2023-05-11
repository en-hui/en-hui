package com.enhui;

import com.enhui.util.RandomUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestData {
  public enum TestType {
    INSERT,
    UPDATE,
    UPSERT,
    DELETE
  }

  private List<String> colNames;
  private List<String> colTypes;
  private List<String> colVals;

  public static List<TestData> listData(List<EntityField> fields, int min, int max) {
    List<TestData> list = new ArrayList<>();
    for (int i = min; i < max; i++) {
      final List<String> colNames = new ArrayList<>(fields.size());
      final List<String> colTypes = new ArrayList<>(fields.size());
      final List<String> colVals = new ArrayList<>(fields.size());
      list.add(new TestData(colNames, colTypes, colVals));
      for (int j = 0; j < fields.size(); j++) {
        final EntityField entityField = fields.get(j);
        final String name = entityField.getName();
        final String type = entityField.getType();
        colNames.add(name);
        colTypes.add(type);
        switch (type) {
          case "int":
            colVals.add(String.valueOf(i));
            break;
          case "varchar":
          default:
            final int random = RandomUtil.randomJust(20);
            colVals.add(
                IntStream.range(0, random)
                    .mapToObj(index -> "这是一个字符串-" + index + ".")
                    .collect(Collectors.joining()));
            break;
        }
      }
    }
    return list;
  }
}
