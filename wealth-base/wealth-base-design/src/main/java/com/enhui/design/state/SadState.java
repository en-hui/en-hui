package com.enhui.design.state;

/**
 * 具体状态-伤心
 * @Author 胡恩会
 * @Date 2020/6/27 21:09
 **/
public class SadState extends MoodState{

    @Override
    void say() {
        System.out.println("哭泣着说话");
    }
}
