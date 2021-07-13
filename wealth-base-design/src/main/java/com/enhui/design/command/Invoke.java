package com.enhui.design.command;

/**
 * 请求者
 *
 * @Author 胡恩会
 * @Date 2020/6/26 22:50
 **/
public class Invoke {
    private ICommand command;

    public Invoke(ICommand command) {
        this.command = command;
    }

    public void setCommand(ICommand command) {
        this.command = command;
    }

    public void executeCommand() {
        command.execute();
    }
}
