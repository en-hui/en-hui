package fun.enhui.thread.jmh;

import org.openjdk.jmh.annotations.*;

/**
 * JMH 使用
 *
 * @Author 胡恩会
 * @Date 2020/8/31 21:49
 **/
public class JMHHelloTest {

    /**
     * 普通遍历的性能
     *
     * @Author: 胡恩会
     * @Date: 2020/8/31 22:31
     * @return: void
     **/
    @Benchmark
    @Warmup(iterations = 1, time = 3)
    @Fork(5)
    @BenchmarkMode(Mode.Throughput)
    @Measurement(iterations = 1, time = 3)
    public void testForEach() {
        JMHHello.foreach();
    }

    /**
     * 并行流遍历的性能
     *
     * @Author: 胡恩会
     * @Date: 2020/8/31 22:33
     * @return: void
     **/
    @Benchmark
    @Warmup(iterations = 1, time = 3)
    @Fork(5)
    @BenchmarkMode(Mode.Throughput)
    @Measurement(iterations = 1, time = 3)
    public void testParallel() {
        JMHHello.parallel();
    }
}
