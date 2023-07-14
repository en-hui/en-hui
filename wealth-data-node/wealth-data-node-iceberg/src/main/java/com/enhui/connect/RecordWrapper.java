package com.enhui.connect;

import org.apache.iceberg.data.Record;
import org.apache.iceberg.types.Types;

import java.util.Map;

public class RecordWrapper implements Record {
    private final Record delegate;
    private final Operation op;

    public RecordWrapper(Record delegate, Operation op) {
        this.delegate = delegate;
        this.op = op;
    }

    public Operation op() {
        return this.op;
    }

    public Types.StructType struct() {
        return this.delegate.struct();
    }

    public Object getField(String name) {
        return this.delegate.getField(name);
    }

    public void setField(String name, Object value) {
        this.delegate.setField(name, value);
    }

    public Object get(int pos) {
        return this.delegate.get(pos);
    }

    public Record copy() {
        return new RecordWrapper(this.delegate.copy(), this.op);
    }

    public Record copy(Map<String, Object> overwriteValues) {
        return new RecordWrapper(this.delegate.copy(overwriteValues), this.op);
    }

    public int size() {
        return this.delegate.size();
    }

    public <T> T get(int pos, Class<T> javaClass) {
        return this.delegate.get(pos, javaClass);
    }

    public <T> void set(int pos, T value) {
        this.delegate.set(pos, value);
    }
}
