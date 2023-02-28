package com.enhui.orc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.exec.vector.BytesColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.LongColumnVector;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;
import org.apache.orc.*;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class OrcDemo {

    String orcFile = OrcDemo.class.getClassLoader().getResource("").getPath()
            .replace("/target/classes/", "/src/main/resources/") + "test.orc";

    @Test
    public void readOrc() throws IOException {
        Configuration conf = new Configuration();
        // Get the information from the file footer
        Reader reader = OrcFile.createReader(new Path(orcFile), OrcFile.readerOptions(conf));
        System.out.println("File schema: " + reader.getSchema());
        System.out.println("Row count: " + reader.getNumberOfRows());

        // Pick the schema we want to read using schema evolution
        // struct<z:int,y:string,x:bigint>
        TypeDescription readSchema = TypeDescription.fromString(reader.getSchema().toString());
        // Read the row data
        VectorizedRowBatch batch = readSchema.createRowBatch();
        RecordReader rowIterator = reader.rows(reader.options().schema(readSchema));
        LongColumnVector z = (LongColumnVector) batch.cols[0];
        BytesColumnVector y = (BytesColumnVector) batch.cols[1];
        LongColumnVector x = (LongColumnVector) batch.cols[2];
        while (rowIterator.nextBatch(batch)) {
            for (int row = 0; row < batch.size; ++row) {
                int zRow = z.isRepeating ? 0 : row;
                int xRow = x.isRepeating ? 0 : row;
                System.out.println("z: " + (z.noNulls || !z.isNull[zRow] ? z.vector[zRow] : null));
                System.out.println("y: " + y.toString(row));
                System.out.println("x: " + (x.noNulls || !x.isNull[xRow] ? x.vector[xRow] : null));
            }
        }
        rowIterator.close();
    }


    @Test
    public void writeOrc() throws IOException {
        Configuration conf = new Configuration();
        TypeDescription schema = TypeDescription.fromString("struct<age:int,name:string,id:bigint>");
        Writer writer = OrcFile.createWriter(new Path(orcFile), OrcFile.writerOptions(conf).setSchema(schema));
        VectorizedRowBatch batch = schema.createRowBatch();
        LongColumnVector age = (LongColumnVector) batch.cols[0];
        BytesColumnVector name = (BytesColumnVector) batch.cols[1];
        LongColumnVector id = (LongColumnVector) batch.cols[2];
        for (int r = 0; r < 10; ++r) {
            int row = batch.size++;
            id.vector[row] = r;
            age.vector[row] = r + 10;
            byte[] buffer = ("Last-" + (r * 3)).getBytes(StandardCharsets.UTF_8);
            name.setRef(row, buffer, 0, buffer.length);
            // If the batch is full, write it out and start over.
            if (batch.size == batch.getMaxSize()) {
                writer.addRowBatch(batch);
                batch.reset();
            }
        }
        if (batch.size != 0) {
            writer.addRowBatch(batch);
        }
        writer.close();
    }

}
