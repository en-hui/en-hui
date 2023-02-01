package com.enhui.netty.research.rpc;

import com.enhui.netty.research.rpc.client.service.LocalFileService;
import com.enhui.netty.research.rpc.client.service.RpcFileService;
import com.enhui.netty.research.rpc.common.network.ClientFactory;
import com.enhui.netty.research.rpc.common.network.ClientPool;
import com.enhui.netty.research.rpc.server.NettyServerApplication;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Warmup(iterations = 1, time = 3)
@Fork(5)
// 设置为线程间共享资源
@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@Measurement(iterations = 1, time = 3)
public class RpcJmhTest {
    InetSocketAddress inetSocketAddress = new InetSocketAddress(NettyServerApplication.SERVICE_A_HOST, NettyServerApplication.SERVICE_A_PORT);
    String fileName = "/Users/liuhe/develop/IdeaPro/wealth/wealth-base-io/src/main/java/com/enhui/netty/research/rpc/file.txt";
    LocalFileService localFileService = new LocalFileService();
    RpcFileService rpcFileService = new RpcFileService();

    @Setup
    public void setUp() throws InterruptedException {
        // 资源初始化，先获取十次，让池子填满
        for (int i = 0; i < 10; i++) {
            ClientFactory.getInstance().getClient(inetSocketAddress);
        }
        ClientPool clientPool = ClientFactory.getInstance().poll.get(inetSocketAddress);
        System.out.println("初始化完成，连接池容器：" + clientPool.clients.length);
    }

    @TearDown
    public void tearDown() {
        ClientFactory.getInstance().close();
    }

    @Benchmark
    public void testLocal() {
        List<String> strings = localFileService.snapshotTable(fileName);
        System.out.println(strings);
    }

    @Benchmark
    public void testRpc() throws ExecutionException, InterruptedException {
        List<String> strings = rpcFileService.snapshotTable(fileName);
        System.out.println(strings);
    }
}
