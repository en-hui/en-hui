package com.enhui.design.command;

/**
 * 具体命令-开灯
 *
 * @Author 胡恩会
 * @Date 2020/6/26 21:43
 **/
public class LightOnCommand implements ICommand {
    LightReceiver receiver = new LightReceiver();

    @Override
    public void execute() {
        receiver.on();
    }

    @Override
    public void undo() {
        receiver.off();
    }

}
