package com.enhui.util;

import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsvUtil {

  /** 读取指定csv文件，将数据返回二维数组。 第一行不会返回，认为第一行是表头 */
  public static Object[][] listData(String filePath) throws IOException {
    CSVReader csvReader = new CSVReader(new FileReader(filePath), ',');
    String[] head = csvReader.readNext();
    log.info("读取csv文件，表头数据为：【{}】", Arrays.toString(head));
    List<String[]> data = csvReader.readAll();
    Object[][] result = new Object[data.size()][head.length];

    for (int row = 0; row < data.size(); row++) {
      String[] line = data.get(row);
      for (int col = 0; col < line.length; col++) {
        result[row][col] = line[col];
      }
    }
    return result;
  }

  public static void main(String[] args) throws IOException {
    Object[][] objects =
        CsvUtil.listData(
            "/Users/huenhui/IdeaProjects/wealth/wealth-auto-test/src/main/resources/excample/simpleDemo.csv");
      for (Object[] object : objects) {
          log.info(Arrays.toString(object));
      }
  }
}
