package com.enhui.connect;

import org.apache.iceberg.FileFormat;
import org.apache.iceberg.PartitionKey;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.data.InternalRecordWrapper;
import org.apache.iceberg.data.Record;
import org.apache.iceberg.io.FileAppenderFactory;
import org.apache.iceberg.io.FileIO;
import org.apache.iceberg.io.OutputFileFactory;
import org.apache.iceberg.io.PartitionedFanoutWriter;

public class PartitionedAppendWriter extends PartitionedFanoutWriter<Record> {
    private final PartitionKey partitionKey;
    private final InternalRecordWrapper wrapper;

    public PartitionedAppendWriter(PartitionSpec spec, FileFormat format, FileAppenderFactory<Record> appenderFactory, OutputFileFactory fileFactory, FileIO io, long targetFileSize, Schema schema) {
        super(spec, format, appenderFactory, fileFactory, io, targetFileSize);
        this.partitionKey = new PartitionKey(spec, schema);
        this.wrapper = new InternalRecordWrapper(schema.asStruct());
    }

    protected PartitionKey partition(Record row) {
        this.partitionKey.partition(this.wrapper.wrap(row));
        return this.partitionKey;
    }
}

