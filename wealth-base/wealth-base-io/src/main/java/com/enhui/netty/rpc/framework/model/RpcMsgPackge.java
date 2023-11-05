package com.enhui.netty.rpc.framework.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RpcMsgPackge {
    private RpcHeader header;
    private RpcContent content;
}
