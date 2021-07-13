package com.enhui.design.bridge;

/**
 * 扩展抽象化角色-数学
 *
 * @Author 胡恩会
 * @Date 2020/6/26 16:43
 **/
public class MathSubject extends Subject {
    public MathSubject(Grade grade) {
        super(grade);
    }

    @Override
    public String getSubjectName() {
        return grade.getGradeName()+"数学";
    }
}
