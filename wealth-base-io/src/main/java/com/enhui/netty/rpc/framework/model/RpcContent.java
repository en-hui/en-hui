package com.enhui.netty.rpc.framework.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcContent implements Serializable {
    private String serviceName;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] args;
    
}
