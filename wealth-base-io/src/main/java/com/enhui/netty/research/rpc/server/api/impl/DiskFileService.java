package com.enhui.netty.research.rpc.server.api.impl;

import com.enhui.netty.research.rpc.common.api.FileSource;
import com.enhui.netty.research.rpc.common.util.FileUtil;

import java.util.List;

public class DiskFileService implements FileSource {

    @Override
    public List<String> snapshotTable(String tableName) {
        return FileUtil.readTextFile(tableName);
    }
}
