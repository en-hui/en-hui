package com.enhui.design.bridge;

/**
 * 抽象化角色-学科
 *
 * @Author 胡恩会
 * @Date 2020/6/26 16:40
 **/
public abstract class Subject {
    Grade grade;
    public Subject(Grade grade){
        this.grade = grade;
    }

    public abstract String getSubjectName();
}
