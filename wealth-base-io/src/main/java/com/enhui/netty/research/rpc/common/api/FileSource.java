package com.enhui.netty.research.rpc.common.api;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface FileSource extends DpSource<String> {
    @Override
    List<String> snapshotTable(String tableName) throws ExecutionException, InterruptedException;
}
