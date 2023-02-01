package com.enhui.netty.research.rpc.client;

import com.enhui.netty.research.rpc.client.service.LocalFileService;
import com.enhui.netty.research.rpc.client.service.RpcFileService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

public class RpcTest {
    String fileName = "/Users/liuhe/develop/IdeaPro/wealth/wealth-base-io/src/main/java/com/enhui/netty/research/rpc/file.txt";
    LocalFileService localFileService = new LocalFileService();
    RpcFileService rpcFileService = new RpcFileService();

    @Test
    public void testLocal() {
        localFileService.snapshotTable(fileName);
    }

    @Test
    public void testRpc() throws ExecutionException, InterruptedException {
        for (int i = 0; i < 10; i++) {
            rpcFileService.snapshotTable(fileName);
        }
    }
}
