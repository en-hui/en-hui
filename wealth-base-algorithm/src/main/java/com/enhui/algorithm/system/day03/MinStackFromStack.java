package com.enhui.algorithm.system.day03;

import com.enhui.algorithm.common.RandomUtil;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

/**
 * 实现一个特殊栈，除了常规都pop和push，在提供一个getMin，可以使用现成的栈结构，要求三个方法的时间复杂度都是O(1)
 */
public class MinStackFromStack {

    public static void main(String[] args) {
        int testTimes = 10000;
        int maxSize = 10;
        int maxValue = 10;
        boolean success = true;
        MinStackFromStack minStack = new MinStackFromStack();
        Stack<Integer> stack = new Stack();
        for (int i = 0; i < testTimes; i++) {
            if (RandomUtil.randomJust(1) == 0) {
                int value = RandomUtil.random(maxValue);
                minStack.push(value);
                stack.push(value);
            } else {
                if (!stack.empty()) {
                    minStack.pop();
                    stack.pop();
                }
            }
            if (stack.empty()) {
                continue;
            }
            if (!Objects.equals(minStack.getMin(), check(stack))) {
                success = false;
                System.out.println("测试失败");
                break;
            }
        }
        if (success) {
            System.out.printf("算法正确，测试次数：「%s」\n", testTimes);
        }
    }

    public static int check(Stack<Integer> dataStack) {
        List<Integer> list = new ArrayList<>();
        Enumeration<Integer> elements = dataStack.elements();
        while (elements.hasMoreElements()) {
            Integer val = elements.nextElement();
            list.add(val);
        }
        return list.stream().sorted().findFirst().get();
    }

    Stack<Integer> dataStack = new Stack<>();
    Stack<Integer> minStack = new Stack<>();

    public void push(int value) {
        dataStack.push(value);
        minStack.push(Math.min(minStack.peek(), value));
    }

    public int pop() {
        minStack.pop();
        return dataStack.pop();
    }

    public int getMin() {
        return minStack.peek();
    }
}
