package com.enhui.netty.rpc.framework.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
public class RpcRequestContent extends RpcContent {
    private String serviceName;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] args;

}
