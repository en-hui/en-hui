package com.enhui.algorithm.system.day03;

import com.enhui.algorithm.common.RandomUtil;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * 如何使用队列结构实现栈结构
 */
public class StackFromQueue {

    public static void main(String[] args) {
        int testTimes = 10000;
        int maxSize = 10;
        int maxValue = 10;
        boolean success = true;
        for (int i = 0; i < testTimes; i++) {
            int size = RandomUtil.randomJust(maxSize);
            Stack<Integer> stack = new Stack<>();
            StackFromQueue stackFromQueue = new StackFromQueue();
            for (int j = 0; j < size; j++) {
                int value = RandomUtil.random(maxValue);
                stack.push(value);
                stackFromQueue.push(value);
            }
            for (int j = 0; j < size; j++) {
                if (!stack.pop().equals(stackFromQueue.pop())) {
                    success = false;
                    break;
                }
            }
            if (!success) {
                System.out.print("测试失败，失败数据：\n");
                System.out.println(Arrays.toString(stack.toArray()));
                System.out.println(Arrays.toString(stackFromQueue.masterQueue.toArray()));
                break;
            }
        }
        if (success) {
            System.out.printf("算法正确，测试次数：「%s」\n", testTimes);
        }
    }

    Queue<Integer> masterQueue = new LinkedList<>();
    Queue<Integer> slaveQueue = new LinkedList<>();
    public void push(int value) {
        masterQueue.offer(value);
    }

    public Integer pop() {
        Integer pre = null;
        Integer cur = null;
        while (true) {
            pre = masterQueue.poll();
            cur = masterQueue.peek();
            if (cur == null) {
                Queue<Integer> temp = masterQueue;
                masterQueue = slaveQueue;
                slaveQueue = temp;
                return pre;
            }
            slaveQueue.offer(pre);
        }
    }
}
