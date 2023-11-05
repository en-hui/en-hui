package com.enhui.netty.rpc.framework.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@AllArgsConstructor
public class RpcResponseContent extends RpcContent {
    private Object result;
}
