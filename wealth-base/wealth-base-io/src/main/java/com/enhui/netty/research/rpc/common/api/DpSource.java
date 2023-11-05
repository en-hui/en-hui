package com.enhui.netty.research.rpc.common.api;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface DpSource<T> {

    List<T> snapshotTable(String tableName) throws ExecutionException, InterruptedException;
}
