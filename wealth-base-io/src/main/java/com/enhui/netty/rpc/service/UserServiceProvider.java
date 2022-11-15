package com.enhui.netty.rpc.service;

import com.enhui.netty.rpc.api.UserApi;
import com.enhui.netty.rpc.model.UserModel;

public class UserServiceProvider implements UserApi {
    @Override
    public void addUser() {
        System.out.println("server add user");
    }

    @Override
    public UserModel getByUserName(String userName) {
        System.out.println("select user by username");
        UserModel userModel = new UserModel(userName, userName + ":nickName1", userName + ":email@qq.com");
        return userModel;
    }
}
