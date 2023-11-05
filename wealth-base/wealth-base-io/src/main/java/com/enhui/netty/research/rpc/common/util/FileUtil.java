package com.enhui.netty.research.rpc.common.util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class FileUtil {

    public static List<String> readTextFile(String fileName) {
        try {
            return Files.readAllLines(Paths.get(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static void main(String[] args) {
        List<String> content = readTextFile("/Users/liuhe/develop/IdeaPro/wealth/wealth-base-io/src/main/java/com/enhui/netty/research/rpc/file.txt");
        System.out.println(String.join("\r\n", content));
    }
}
