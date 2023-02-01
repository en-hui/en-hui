package com.enhui.netty.research.rpc.client.controller;

import com.enhui.netty.research.rpc.client.service.LocalFileService;
import com.enhui.netty.research.rpc.client.service.RpcFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
public class FileController {

    @Autowired
    private LocalFileService localFileService;
    @Autowired
    private RpcFileService rpcFileService;

    @GetMapping("/rpc")
    public void rpcGetFile(@RequestParam("fileName") String fileName) throws ExecutionException, InterruptedException {
        log.info("rpc get file");
        List<String> lines = rpcFileService.snapshotTable(fileName);
        log.info("rpc get file:\n{}", String.join("\r\n", lines));
    }

    @GetMapping("/local")
    public void localGetFile(@RequestParam("fileName") String fileName) {
        log.info("local get file");
        List<String> lines = localFileService.snapshotTable(fileName);
        log.info("local get file:\n{}", String.join("\r\n", lines));
    }
}
