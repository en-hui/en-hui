package com.enhui.design.templatemethod;

/**
 * 具体子类
 * @Author 胡恩会
 * @Date 2020/6/27 17:04
 **/
public class PageLoad extends BasePageLoadTemplate {
    @Override
    public void init() {
        System.out.println("页面初始化");
    }

    @Override
    public void loadContent() {
        System.out.println("加载页面内容");
    }
}
