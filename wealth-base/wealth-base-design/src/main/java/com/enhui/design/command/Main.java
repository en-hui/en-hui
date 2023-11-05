package com.enhui.design.command;

/**
 * @Author 胡恩会
 * @Date 2020/6/26 22:02
 **/
public class Main {
    public static void main(String[] args) {
        Invoke invoke = new Invoke(new LightOnCommand());
        invoke.executeCommand();

        invoke.setCommand(new LightOffCommand());
        invoke.executeCommand();
    }
}
