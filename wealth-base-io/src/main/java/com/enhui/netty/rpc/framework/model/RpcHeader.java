package com.enhui.netty.rpc.framework.model;

import lombok.Data;
import lombok.ToString;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@Data
@ToString
public class RpcHeader implements Serializable {
    public static final int headerLen = getLen();
    private long requestId;
    private long dataLen;

    private static int getLen() {
        try {
            RpcHeader header = new RpcHeader();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream oout = new ObjectOutputStream(out);
            oout.writeObject(header);
            byte[] msgHeader = out.toByteArray();
            System.out.println("header len :" + msgHeader.length);
            return msgHeader.length;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
