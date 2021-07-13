package com.enhui.design.bridge;

/**
 * @Author 胡恩会
 * @Date 2020/6/26 16:46
 **/
public class Main {
    public static void main(String[] args) {
        MathSubject mathSubject = new MathSubject(new OneGrade());
        System.out.println(mathSubject.getSubjectName());
    }
}
