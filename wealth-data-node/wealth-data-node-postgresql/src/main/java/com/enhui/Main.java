package com.enhui;

import java.util.Scanner;
import org.postgresql.replication.LogSequenceNumber;

public class Main {
  public static void main(String[] args) {

    Scanner scanner = new Scanner(System.in);
    boolean continueExecute = true;
    while (continueExecute) {
      System.out.println("输入0即退出；输入十六进制lsn完成转换，例如：A/1A8FD18");
      System.out.println("请输入你的内容：");
      final String next = scanner.next();
      switch (next) {
        case "0":
          continueExecute = false;
          break;
        default:
          LogSequenceNumber logSequenceNumber = LogSequenceNumber.valueOf(next);
          System.out.println(logSequenceNumber.asLong());
          break;
      }
    }
  }
}
