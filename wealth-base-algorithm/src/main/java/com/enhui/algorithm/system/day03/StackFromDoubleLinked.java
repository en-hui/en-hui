package com.enhui.algorithm.system.day03;

import com.enhui.algorithm.common.DoubleLinkedNode;
import com.enhui.algorithm.common.RandomUtil;

import java.util.Arrays;
import java.util.Stack;

import static com.enhui.algorithm.common.DoubleLinkedNode.printLinked;


/**
 * 双向链表实现栈
 */
public class StackFromDoubleLinked {
    public static void main(String[] args) {
        int testTimes = 10000;
        int maxSize = 10;
        int maxValue = 10;
        boolean success = true;
        for (int i = 0; i < testTimes; i++) {
            Stack<Integer> stack = new Stack<>();
            StackFromDoubleLinked stackFromDoubleLinked = new StackFromDoubleLinked();
            int size = RandomUtil.randomJust(maxSize);
            for (int j = 0; j < size; j++) {
                int value = RandomUtil.random(maxValue);
                stack.push(value);
                stackFromDoubleLinked.push(value);
            }
            for (int j = 0; j < size; j++) {
                if (!stack.pop().equals(stackFromDoubleLinked.pop())) {
                    success = false;
                    break;
                }
            }
            if (!success) {
                System.out.print("测试失败，失败数据：\n");
                System.out.println(Arrays.toString(stack.toArray()));
                printLinked(stackFromDoubleLinked.head);
                break;
            }
        }
        if (success) {
            System.out.printf("算法正确，测试次数：「%s」\n", testTimes);
        }
    }

    DoubleLinkedNode head;

    public void push(int value) {
        DoubleLinkedNode newNode = new DoubleLinkedNode(value);
        if (head == null) {
            head = newNode;
        } else {
            head.last = newNode;
            newNode.next = head;
            head = newNode;
        }

    }

    public Integer pop() {
        if (head != null) {
            int value = head.getValue();
            head = head.next;
            if (head != null) {
                head.last = null;
            }
            return value;
        } else {
            return null;
        }
    }
}
