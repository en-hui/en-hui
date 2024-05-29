package com.enhui;

import com.enhui.jmh.StringReplace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@Slf4j
public class JMHStringReplace {

  @Benchmark
  public void testReplace() {
    final String txt = String.join(",", Collections.nCopies(50, "我是字符串"));
    StringReplace.removeNullChar(txt, true);
  }

  @Benchmark
  public void testReplaceNew() {
    final String txt = String.join(",", Collections.nCopies(50, "我是字符串"));
    StringReplace.removeNullCharNew(txt, true);
  }

  /**
   * 需要插件：https://plugins.jetbrains.com/plugin/7529-jmh-java-microbenchmark-harness
   *
   * @param args
   * @throws RunnerException
   */
  public static void main(String[] args) throws RunnerException {
    Options opt =
        new OptionsBuilder()
            .include(JMHStringReplace.class.getSimpleName())
            .forks(1)
            .warmupIterations(1)
            .measurementIterations(3)
            .output("./Benchmark.log")
            .build();

    new Runner(opt).run();
  }
}
