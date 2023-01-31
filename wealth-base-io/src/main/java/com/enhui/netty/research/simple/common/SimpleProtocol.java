package com.enhui.netty.research.simple.common;

import com.enhui.netty.research.simple.common.proto.MsgProto;
import com.google.protobuf.GeneratedMessageV3;
import lombok.Data;

/** 自定义协议 */
@Data
public class SimpleProtocol {
  // 版本
  private String version;
  // 序列化方式
  private SerdeType serdeType;
  // 指令类型（登陆、注册）
  private MsgProto.MsgCode msgCode;
  private String requestId;
  private Long contentLength;
  private GeneratedMessageV3 content;

  enum SerdeType {
    PROTOBUF // protobuf
  }
}
