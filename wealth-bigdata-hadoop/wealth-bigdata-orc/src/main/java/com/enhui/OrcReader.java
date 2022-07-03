package com.enhui;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;
import org.apache.orc.*;
import org.testng.annotations.Test;

import java.io.IOException;


public class OrcReader {


    @Test
    public void readOrc() throws IOException {
        String pathStr = OrcReader.class.getClassLoader().getResource("").getPath() + "orc_all_type";

        System.out.println(pathStr);

        Path path = new Path(pathStr);
        OrcFile.ReaderOptions op = OrcFile.readerOptions(new Configuration());
        Reader reader = OrcFile.createReader(path,op);
        TypeDescription schema = reader.getSchema();
        VectorizedRowBatch rowBatch = reader.getSchema().createRowBatch();

    }

    @Test
    public void writeOrc() throws IOException {
        Configuration conf = new Configuration();
        TypeDescription schema = TypeDescription.fromString("struct<x:int,y:int>");
        Writer writer = OrcFile.createWriter(new Path("my-file.orc"),
                OrcFile.writerOptions(conf)
                        .setSchema(schema));
    }

}
