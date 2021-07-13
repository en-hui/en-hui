package com.enhui.algorithm.左神算法.基础班.day02;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 用两个队列实现栈效果
 * 一个data队列，一个help队列
 * 每次存数据向data队列存，
 * 取数据时，将data队列中数据倒到help队列，只留一个返回出去。然后在倒回来
 *
 * @author 胡恩会
 * @date 2021/1/2 11:29
 */
public class Code07_TwoQueueImplementStack {
    private Queue<Integer> dataQueue;

    private Queue<Integer> helpQueue;

    public Code07_TwoQueueImplementStack() {
        this.dataQueue = new LinkedList<>();
        this.helpQueue = new LinkedList<>();
    }

    public void push(Integer value) {
        this.dataQueue.add(value);
    }

    public Integer pop() {
        if (this.dataQueue.isEmpty()) {
            throw new RuntimeException("栈已经空了");
        }
        while (dataQueue.size() > 1) {
            helpQueue.add(dataQueue.poll());
        }
        Integer result = dataQueue.poll();
        while (helpQueue.size() > 0) {
            dataQueue.add(helpQueue.poll());
        }
        return result;
    }

    public static void main(String[] args) {
        int count = 5;
        Code07_TwoQueueImplementStack stack = new Code07_TwoQueueImplementStack();
        for (int i = 0; i < count; i++) {
            stack.push(i);
        }
        for (int i = 0; i < count; i++) {
            System.out.println(stack.pop());
        }
    }
}
