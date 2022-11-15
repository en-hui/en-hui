package com.enhui.netty.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserModel {
    private String userName;
    private String nickName;
    private String email;
}
