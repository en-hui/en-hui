package com.enhui.connect;

import org.apache.iceberg.FileFormat;
import org.apache.iceberg.PartitionKey;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.StructLike;
import org.apache.iceberg.data.InternalRecordWrapper;
import org.apache.iceberg.data.Record;
import org.apache.iceberg.io.BaseTaskWriter;
import org.apache.iceberg.io.FileAppenderFactory;
import org.apache.iceberg.io.FileIO;
import org.apache.iceberg.io.OutputFileFactory;
import org.apache.iceberg.types.TypeUtil;
import java.io.IOException;
import org.apache.iceberg.relocated.com.google.common.collect.Sets;

public abstract class BaseDeltaTaskWriter extends BaseTaskWriter<Record> {
    private final Schema schema;
    private final Schema deleteSchema;
    private final InternalRecordWrapper wrapper;
    private final InternalRecordWrapper keyWrapper;
    private final boolean upsertMode;

    BaseDeltaTaskWriter(PartitionSpec spec, FileFormat format, FileAppenderFactory<Record> appenderFactory, OutputFileFactory fileFactory, FileIO io, long targetFileSize, Schema schema, boolean upsertMode) {
        super(spec, format, appenderFactory, fileFactory, io, targetFileSize);
        this.schema = schema;
        this.deleteSchema = TypeUtil.select(schema, Sets.newHashSet(schema.identifierFieldIds()));
        this.wrapper = new InternalRecordWrapper(schema.asStruct());
        this.keyWrapper = new InternalRecordWrapper(this.deleteSchema.asStruct());
        this.upsertMode = upsertMode;
    }

    abstract RowDataDeltaWriter route(Record var1);

    InternalRecordWrapper wrapper() {
        return this.wrapper;
    }

    public void write(Record row) throws IOException {
        Operation op = row instanceof RecordWrapper ? ((RecordWrapper)row).op() : (this.upsertMode ? Operation.UPDATE : Operation.INSERT);
        RowDataDeltaWriter writer = this.route(row);
        if (op == Operation.UPDATE || op == Operation.DELETE) {
            writer.delete(row);
        }

        if (op == Operation.UPDATE || op == Operation.INSERT) {
            writer.write(row);
        }

    }

    class RowDataDeltaWriter extends BaseTaskWriter<Record>.BaseEqualityDeltaWriter {
        RowDataDeltaWriter(PartitionKey partition) {
            super(partition, BaseDeltaTaskWriter.this.schema, BaseDeltaTaskWriter.this.deleteSchema);
        }

        protected StructLike asStructLike(Record data) {
            return BaseDeltaTaskWriter.this.wrapper.wrap(data);
        }

        protected StructLike asStructLikeKey(Record data) {
            return BaseDeltaTaskWriter.this.keyWrapper.wrap(data);
        }
    }
}
