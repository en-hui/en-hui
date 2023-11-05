package com.enhui.netty.tinygame.herostory;

import com.enhui.netty.tinygame.herostory.msg.GameMsgProtocol;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息识别器
 */
@Slf4j
public final class GameMsgRecognizer {
    /**
     * 消息代码和消息体字典
     */
    static private final Map<Integer, GeneratedMessageV3> _msgCodeAndMsgBodyMap = new HashMap<>();
    /**
     * 消息类型和消息编号字典
     */
    static private final Map<Class<?>, Integer> _msgClazzAndMsgCodeMap = new HashMap<>();
    /**
     * 私有化类默认构造器
     */
    private GameMsgRecognizer() {
    }
    /**
     * 初始化
     */
    static public void init() {
        log.info("==== 完成 MsgBody 和 MsgCode 的关联 ====");
        // 获取所有的内部类
        Class<?>[] innerClazzArray = GameMsgProtocol.class.getDeclaredClasses();
        for (Class<?> innerClazz : innerClazzArray) {
            if (!GeneratedMessageV3.class.isAssignableFrom(innerClazz)) {
                // 如果不是消息,
                continue;
            }
            // 获取类名称并转成小写
            String clazzName = innerClazz.getSimpleName();
            clazzName = clazzName.toLowerCase();
            // 接下来遍历 MsgCode 枚举
            for (GameMsgProtocol.MsgCode msgCode : GameMsgProtocol.MsgCode.values()) {
                // 获取枚举名称并转成小写
                String strMsgCode = msgCode.name();
                strMsgCode = strMsgCode.replaceAll("_", "");
                strMsgCode = strMsgCode.toLowerCase();
                if (!strMsgCode.startsWith(clazzName)) {
                    continue;
                }
                // 消息编号名称和类名称正好相同,
                // 则建立关系
                try {
                    // 调用 XxxCmd 或者 XxxResult 的 getDefaultInstance 静态方法,
                    // 目的是返回默认实例
                    Object returnObj = innerClazz.getDeclaredMethod("getDefaultInstance").invoke(innerClazz);
                    log.info(
                            "关联 {} <==> {}",
                            innerClazz.getName(),
                            msgCode.getNumber()
                    );
                    // 关联消息编号与消息体
                    _msgCodeAndMsgBodyMap.put(
                            msgCode.getNumber(),
                            (GeneratedMessageV3) returnObj
                    );
                    // 关联消息类与消息编号
                    _msgClazzAndMsgCodeMap.put(
                            innerClazz,
                            msgCode.getNumber()
                    );
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
        }
    }
    /**
     * 根据消息编号获取消息构建者
     *
     * @param msgCode 消息编号
     * @return 消息构建者
     */
    static public Message.Builder getMsgBuilderByMsgCode(int msgCode) {
        if (msgCode < 0) {
            return null;
        }
        // 获取消息对象
        GeneratedMessageV3 msg = _msgCodeAndMsgBodyMap.get(msgCode);
        if (null == msg) {
            return null;
        }
        return msg.newBuilderForType();
    }
    /**
     * 根据消息类获取消息编号
     *
     * @param msgClazz 消息类
     * @return 消息编号
     */
    static public int getMsgCodeByMsgClazz(Class<?> msgClazz) {
        if (null == msgClazz) {
            return -1;
        }
        // 获取消息编号
        Integer msgCode = _msgClazzAndMsgCodeMap.get(msgClazz);
        if (null != msgCode) {
            return msgCode.intValue();
        } else {
            return -1;
        }
    }
}

