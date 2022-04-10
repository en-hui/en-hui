package com.enhui.mapreduce.data;

import java.io.FileWriter;
import java.io.IOException;

public class WordCountTest {
  public static void main(String[] args) throws IOException {

    try (FileWriter writer =
        new FileWriter("./wealth-bigdata-mr/src/test/resources/wordCount.txt"); ) {
      for (int i = 0; i < 100000; i++) {
        writer.append("hello word hello hadoop " + i + "\n");
      }
    }
  }
}
