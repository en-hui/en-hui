package com.enhui;

import com.enhui.jmh.StringReplace;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.nio.charset.StandardCharsets;

@Slf4j
public class JMHStringReplace {
    String txt = "我是字符串,我是字符串,我是字符串,我是字符串,我是字符串,我是字符串,我是字符串,我是字符串,我是字符串,我是字符串,我是字符串";

    public void testReplace() {
        StringReplace.removeNullChar(txt, true);
    }

    public void testReplaceNew() {
        StringReplace.removeNullCharNew(txt, true);
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMHStringReplace.class.getSimpleName())
                .forks(1)
                .warmupIterations(1)
                .measurementIterations(3)
                .output("./Benchmark.log")
                .build();

        new Runner(opt).run();
    }


}
