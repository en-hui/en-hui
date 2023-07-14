package com.enhui.connect;

import org.apache.iceberg.FileFormat;
import org.apache.iceberg.PartitionKey;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.data.Record;
import org.apache.iceberg.io.FileAppenderFactory;
import org.apache.iceberg.io.FileIO;
import org.apache.iceberg.io.OutputFileFactory;

import java.io.IOException;

public class UnpartitionedDeltaWriter extends BaseDeltaTaskWriter {
    private final BaseDeltaTaskWriter.RowDataDeltaWriter writer = new BaseDeltaTaskWriter.RowDataDeltaWriter((PartitionKey)null);

    public UnpartitionedDeltaWriter(PartitionSpec spec, FileFormat format, FileAppenderFactory<Record> appenderFactory, OutputFileFactory fileFactory, FileIO io, long targetFileSize, Schema schema, boolean upsertMode) {
        super(spec, format, appenderFactory, fileFactory, io, targetFileSize, schema, upsertMode);
    }

    BaseDeltaTaskWriter.RowDataDeltaWriter route(Record row) {
        return this.writer;
    }

    public void close() throws IOException {
        this.writer.close();
    }
}
