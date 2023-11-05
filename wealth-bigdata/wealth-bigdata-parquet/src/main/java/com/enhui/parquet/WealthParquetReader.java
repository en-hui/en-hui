package com.enhui.parquet;

import org.apache.hadoop.conf.Configuration;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.api.ReadSupport;
import org.apache.parquet.hadoop.example.GroupReadSupport;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.PrimitiveType;
import org.apache.parquet.schema.Type;
import org.apache.parquet.schema.Types;

import java.util.ArrayList;
import java.util.List;

public class WealthParquetReader {

    Configuration conf;

    public static void main(String[] args) {
        String filePath = "";
        final List<String> lines = readParquetFile(filePath);
        for (String line : lines) {
            System.out.println(line);
        }
    }

    public static List<String> readParquetFile(String filePath) {
        List<String> lines = new ArrayList<>();

        return lines;
    }
}
