package com.enhui.netty.tinygame.herostory.cmdHandle;

import com.enhui.netty.tinygame.herostory.Broadcaster;
import com.enhui.netty.tinygame.herostory.msg.GameMsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * 用户攻击指令处理器
 */
public class UserAttkCmdHandler implements ICmdHandler<GameMsgProtocol.UserAttkCmd> {
    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserAttkCmd cmd) {
        if (null == ctx ||
                null == cmd) {
            return;
        }
        // 获取攻击者 Id
        Integer attkUserId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (null == attkUserId) {
            return;
        }
        // 获取被攻击者 Id
        int targetUserId = cmd.getTargetUserId();
        GameMsgProtocol.UserAttkResult.Builder resultBuilder = GameMsgProtocol.UserAttkResult.newBuilder();
        resultBuilder.setAttkUserId(attkUserId);
        resultBuilder.setTargetUserId(targetUserId);
        GameMsgProtocol.UserAttkResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);
        // 减血消息, 可以根据自己的喜好写...
        // 例如加上装备加成, 暴击等等.
        // 这些都属于游戏的业务逻辑了!
        GameMsgProtocol.UserSubtractHpResult.Builder resultBuilder2 = GameMsgProtocol.UserSubtractHpResult.newBuilder();
        resultBuilder2.setTargetUserId(targetUserId);
        resultBuilder2.setSubtractHp(10);
        GameMsgProtocol.UserSubtractHpResult newResult2 = resultBuilder2.build();
        Broadcaster.broadcast(newResult2);
    }
}

