package com.enhui.connect;

import org.apache.iceberg.FileFormat;
import org.apache.iceberg.PartitionKey;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.data.Record;
import org.apache.iceberg.io.BaseTaskWriter;
import org.apache.iceberg.io.FileAppenderFactory;
import org.apache.iceberg.io.FileIO;
import org.apache.iceberg.io.OutputFileFactory;
import org.apache.iceberg.relocated.com.google.common.collect.Maps;
import org.apache.iceberg.util.Tasks;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;

public class PartitionedDeltaWriter extends BaseDeltaTaskWriter {
    private final PartitionKey partitionKey;
    private final Map<PartitionKey, RowDataDeltaWriter> writers = Maps.newHashMap();

    public PartitionedDeltaWriter(PartitionSpec spec, FileFormat format, FileAppenderFactory<Record> appenderFactory, OutputFileFactory fileFactory, FileIO io, long targetFileSize, Schema schema, boolean upsertMode) {
        super(spec, format, appenderFactory, fileFactory, io, targetFileSize, schema, upsertMode);
        this.partitionKey = new PartitionKey(spec, schema);
    }

    BaseDeltaTaskWriter.RowDataDeltaWriter route(Record row) {
        this.partitionKey.partition(this.wrapper().wrap(row));
        BaseDeltaTaskWriter.RowDataDeltaWriter writer = (BaseDeltaTaskWriter.RowDataDeltaWriter)this.writers.get(this.partitionKey);
        if (writer == null) {
            PartitionKey copiedKey = this.partitionKey.copy();
            writer = new BaseDeltaTaskWriter.RowDataDeltaWriter(copiedKey);
            this.writers.put(copiedKey, writer);
        }

        return writer;
    }

    public void close() {
        try {
            Tasks.foreach(this.writers.values()).throwFailureWhenFinished().noRetry().run(BaseTaskWriter.BaseEqualityDeltaWriter::close, IOException.class);
            this.writers.clear();
        } catch (IOException var2) {
            throw new UncheckedIOException("Failed to close equality delta writer", var2);
        }
    }
}
