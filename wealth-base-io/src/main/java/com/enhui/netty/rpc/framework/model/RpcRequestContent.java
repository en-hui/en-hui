package com.enhui.netty.rpc.framework.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class RpcRequestContent implements Serializable {
    private String serviceName;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] args;

}
