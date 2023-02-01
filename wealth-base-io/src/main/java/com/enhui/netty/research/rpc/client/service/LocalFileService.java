package com.enhui.netty.research.rpc.client.service;

import com.enhui.netty.research.rpc.common.api.FileSource;
import com.enhui.netty.research.rpc.common.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class LocalFileService implements FileSource {

    @Override
    public List<String> snapshotTable(String tableName) {
        log.info("local 模式，调用本地方法");
        return FileUtil.readTextFile(tableName);
    }

}
