package com.enhui.netty.research.rpc.client;

import com.enhui.netty.research.rpc.client.service.LocalFileService;
import com.enhui.netty.research.rpc.client.service.RpcFileService;
import com.enhui.netty.research.rpc.common.network.ClientFactory;
import com.enhui.netty.research.rpc.common.network.ClientPool;
import com.enhui.netty.research.rpc.server.NettyServerApplication;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @BenchmarkMode <br>
 * Throughput：整体吞吐量，每秒执行了多少次调用，单位为 ops/time
 * AverageTime：用的平均时间，每次操作的平均时间，单位为 time/op
 * SampleTime：随机取样，最后输出取样结果的分布
 * SingleShotTime：只运行一次，往往同时把 Warmup 次数设为 0，用于测试冷启动时的性能
 * All：上面的所有模式都执行一次
 * <p>
 * @State <br>
 * Scope.Benchmark：所有测试线程共享一个实例，测试有状态实例在多线程共享下的性能
 * Scope.Group：同一个线程在同一个 group 里共享实例
 * Scope.Thread：默认的 State，每个测试线程分配一个实例
 * <p>
 * @Warmup <br>
 * iterations：预热的次数
 * time：每次预热的时间
 * timeUnit：时间的单位，默认秒
 * batchSize：批处理大小，每次操作调用几次方法
 * <p>
 * Benchmark              Mode  Cnt   Score   Error   Units
 * RpcJmhTest.testLocal  thrpt    2  65.551          ops/ms
 * RpcJmhTest.testRpc    thrpt    3  10.768 ± 5.126  ops/ms
 * Benchmark：测试方法
 * Mode：测试的模式，由 @BenchmarkMode 决定
 * Cnt：测试次数，由 @Measurement(iterations = 3, time = 3) 的iterations决定
 * Score：分值（性能结果）
 * Error：误差
 * Units：单位，由 @OutputTimeUnit 决定
 */
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class RpcJmhTest {


    @Benchmark
    @Warmup(iterations = 1, time = 3)
    @Fork(1)
    @BenchmarkMode(Mode.Throughput)
    @Measurement(iterations = 3, time = 3)
    public void testRpc() throws ExecutionException, InterruptedException {
        rpcFileService.snapshotTable(fileName);
    }

    @Benchmark
    @Warmup(iterations = 1, time = 3)
    @Fork(1)
    @BenchmarkMode(Mode.Throughput)
    @Measurement(iterations = 2, time = 3)
    public void testLocal() {
        localFileService.snapshotTable(fileName);
    }

    String fileName;
    LocalFileService localFileService;
    RpcFileService rpcFileService;

    @Setup
    public void setUp() throws InterruptedException {
        fileName = "/Users/liuhe/develop/IdeaPro/wealth/wealth-base-io/src/main/java/com/enhui/netty/research/rpc/file.txt";
        localFileService = new LocalFileService();
        rpcFileService = new RpcFileService();

        InetSocketAddress inetSocketAddress = new InetSocketAddress(NettyServerApplication.SERVICE_A_HOST, NettyServerApplication.SERVICE_A_PORT);
        // 资源初始化，先获取十次，让池子填满
        for (int i = 0; i < 10; i++) {
            ClientFactory.getInstance().getClient(inetSocketAddress);
        }
        ClientPool clientPool = ClientFactory.getInstance().poll.get(inetSocketAddress);
        System.out.println("jmh :: 初始化完成，连接池容量：" + clientPool.clients.length);
    }

    @TearDown
    public void tearDown() {
        ClientFactory.getInstance().close();
        System.out.println("jmh :: 资源销毁完毕");
    }
}
