package com.enhui.netty.rpc.framework.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcHeader implements Serializable {
    private long requestId;
    private long dataLen;
}
