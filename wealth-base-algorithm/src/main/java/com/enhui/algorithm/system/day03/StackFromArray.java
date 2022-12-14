package com.enhui.algorithm.system.day03;

import com.enhui.algorithm.common.RandomUtil;

import java.util.Arrays;
import java.util.Stack;


/**
 * 数组实现栈
 */
public class StackFromArray {

    public static void main(String[] args) {
        int testTimes = 10000;
        int maxSize = 10;
        int maxValue = 10;
        boolean success = true;
        for (int i = 0; i < testTimes; i++) {
            int size = RandomUtil.randomJust(maxSize);
            Stack<Integer> stack = new Stack<>();
            StackFromArray stackFromArray = new StackFromArray(size);
            for (int j = 0; j < size; j++) {
                int value = RandomUtil.random(maxValue);
                stack.push(value);
                stackFromArray.push(value);
            }
            for (int j = 0; j < size; j++) {
                if (!stack.pop().equals(stackFromArray.pop())) {
                    success = false;
                    break;
                }
            }
            if (!success) {
                System.out.print("测试失败，失败数据：\n");
                System.out.println(Arrays.toString(stack.toArray()));
                System.out.println(Arrays.toString(stackFromArray.arr));
                break;
            }
        }
        if (success) {
            System.out.printf("算法正确，测试次数：「%s」\n", testTimes);
        }
    }


    int[] arr = null;
    int capacity = -1;
    int position = -1;

    public StackFromArray(int capacity) {
        this.capacity = capacity;
        this.position = 0;
        arr = new int[capacity];
    }

    public void push(int value) {
        if (position >= capacity) {
            // 满了，不能放了就不放了
            return;
        }
        arr[position++] = value;
    }

    public Integer pop() {
        if (position <= 0) {
            return null;
        }
        return arr[--position];
    }
}
