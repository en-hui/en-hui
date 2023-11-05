package com.enhui.netty.rpc.provider;

import com.enhui.netty.rpc.api.UserApi;

public class UserServiceProvider implements UserApi {

    @Override
    public String getByUserName(String userName) {
        // 真正的方法实现
        return "result_" + userName;
    }
}
