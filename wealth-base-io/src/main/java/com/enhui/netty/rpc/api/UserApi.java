package com.enhui.netty.rpc.api;

import com.enhui.netty.rpc.model.UserModel;

public interface UserApi {

    void addUser();

    UserModel getByUserName(String userName);
}
