package com.enhui.parquet;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.column.ParquetProperties;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.GroupFactory;
import org.apache.parquet.example.data.simple.Int96Value;
import org.apache.parquet.example.data.simple.NanoTime;
import org.apache.parquet.example.data.simple.SimpleGroupFactory;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.example.GroupWriteSupport;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.io.api.Binary;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.OriginalType;
import org.apache.parquet.schema.PrimitiveType;
import org.apache.parquet.schema.Types;

public class WealthParquetWriter {

  public static void main(String[] args) throws IOException {
    final Random random = new Random();
    String filePath =
        "/Users/huenhui/IdeaProjects/wealth/wealth-bigdata-hadoop/wealth-bigdata-parquet/src/main/resources/int96_"
            + random.nextInt(100)
            + ".parquet";
    writeParquetFile(filePath);
  }

  public static void writeParquetFile(String filePath) throws IOException {
    final MessageType messageType =
        Types.buildMessage()
//            .required(PrimitiveType.PrimitiveTypeName.INT32)
//            .named("col_int32")
//            .required(PrimitiveType.PrimitiveTypeName.INT64)
//            .named("col_int64")
            .required(PrimitiveType.PrimitiveTypeName.INT96)
            .named("col_int96")
//            .required(PrimitiveType.PrimitiveTypeName.BINARY)
//            .as(OriginalType.UTF8)
//            .named("col_binary")
//            .required(PrimitiveType.PrimitiveTypeName.BOOLEAN)
//            .named("col_bool")
//            .required(PrimitiveType.PrimitiveTypeName.FLOAT)
//            .named("col_float")
//            .required(PrimitiveType.PrimitiveTypeName.DOUBLE)
//            .named("col_double")
//            .required(PrimitiveType.PrimitiveTypeName.FIXED_LEN_BYTE_ARRAY)
//            .length(10)
//            .named("col_fixed")
            .named("AllType");

    GroupFactory factory = new SimpleGroupFactory(messageType);
    Path path = new Path(filePath);
    Configuration configuration = new Configuration();
    GroupWriteSupport writeSupport = new GroupWriteSupport();
    GroupWriteSupport.setSchema(messageType, configuration);

    //    ParquetWriter<Group> writer = new ParquetWriter<Group>(path, configuration, writeSupport);

    ParquetWriter<Group> writer =
        new ParquetWriter<Group>(
            path,
            writeSupport,
            CompressionCodecName.UNCOMPRESSED,
            ParquetWriter.DEFAULT_BLOCK_SIZE,
            ParquetWriter.DEFAULT_PAGE_SIZE,
            1048576,
            true,
            false,
            ParquetProperties.WriterVersion.PARQUET_1_0,
            configuration);

    final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
    System.out.println(timestamp);

    Group group =
        factory
            .newGroup()
//            .append("col_int32", 1)
//            .append("col_int64", "1")
            .append(
                "col_int96",
                NanoTime.fromInt96(new Int96Value(Binary.fromString("2023-03-02 11:42:40.389"))));
//            .append("col_binary", Binary.fromString("hello"))
//            .append("col_bool", true)
//            .append("col_float", 1.1f)
//            .append("col_double", 2.2d);
//            .append("col_fixed", "fixed_len_byte_array");
    writer.write(group);
    writer.close();
  }
}
