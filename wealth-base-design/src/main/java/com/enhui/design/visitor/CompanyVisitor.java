package com.enhui.design.visitor;

/**
 * 具体访问者-企业用户
 *
 * @Author 胡恩会
 * @Date 2020/6/26 14:38
 **/
public class CompanyVisitor implements IComputerVisitor {
    @Override
    public void visitorCpu(Cpu cpu) {
        System.out.println("企业用户购买cpu打8折");
    }
    @Override
    public void visitorMemory(Memory memory) {
        System.out.println("企业用户购买内存打8.5折");
    }
}
