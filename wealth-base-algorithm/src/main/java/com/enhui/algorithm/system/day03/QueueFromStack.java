package com.enhui.algorithm.system.day03;

import com.enhui.algorithm.common.RandomUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Stack;

/**
 * 如何使用栈结构实现队列结构
 */
public class QueueFromStack {

    public static void main(String[] args) {
        int testTimes = 10000;
        int maxSize = 10;
        int maxValue = 10;
        boolean success = true;
        for (int i = 0; i < testTimes; i++) {
            int size = RandomUtil.randomJust(maxSize);
            Queue<Integer> queue = new LinkedList<>();
            QueueFromStack queueFromStack = new QueueFromStack();
            List<Integer> errorRecord = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                int value = RandomUtil.random(maxValue);
                errorRecord.add(value);
                queue.offer(value);
                queueFromStack.offer(value);
            }
            for (int j = 0; j < size; j++) {
                Integer poll = queue.poll();
                Integer poll1 = queueFromStack.poll();
                if (!Objects.equals(poll, poll1)) {
                    System.out.printf("测试失败，失败数据：size:%s,{%s} ,%s--%s\n", size, errorRecord, poll, poll1);
                    success = false;
                    break;
                }
            }
            if (!success) {
                break;
            }
        }
        if (success) {
            System.out.printf("算法正确，测试次数：「%s」\n", testTimes);
        }
    }

    Stack<Integer> pushStack = new Stack<>();
    Stack<Integer> popStack = new Stack<>();

    public void offer(Integer value) {
        pushStack.push(value);
        moveData();
    }

    public Integer poll() {
        moveData();
        return popStack.pop();
    }

    public synchronized void moveData() {
        if (popStack.empty()) {
            while (!pushStack.empty()) {
                Integer val = pushStack.pop();
                popStack.push(val);
            }
        }
    }
}
