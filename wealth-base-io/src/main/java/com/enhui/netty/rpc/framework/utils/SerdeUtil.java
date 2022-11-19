package com.enhui.netty.rpc.framework.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SerdeUtil {
    private static ByteArrayOutputStream out = new ByteArrayOutputStream();

    public synchronized static byte[] serde(Object content) throws IOException {
        out.reset();
        ObjectOutputStream oout = new ObjectOutputStream(out);
        oout.writeObject(content);
        return out.toByteArray();
    }
}
