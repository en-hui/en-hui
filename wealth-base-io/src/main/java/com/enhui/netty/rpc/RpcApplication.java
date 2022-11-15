package com.enhui.netty.rpc;

import com.enhui.netty.rpc.api.UserApi;
import com.enhui.netty.rpc.framework.proxy.JdkProxy;
import org.junit.jupiter.api.Test;

public class RpcApplication {
  /** 服务提供者 */
  @Test
  public void provider() {}

  /** 服务消费者 */
  @Test
  public void consumer() {
    UserApi userApi = JdkProxy.proxyGet(UserApi.class);
  }
}
