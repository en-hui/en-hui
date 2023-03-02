package com.enhui.algorithm.system.day03;

import com.enhui.algorithm.util.RandomUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

/**
 * 数组实现队列
 */
public class QueueFromArray {

    public static void main(String[] args) {
        int testTimes = 10000;
        int maxSize = 10;
        int maxValue = 10;
        boolean success = true;
        for (int i = 0; i < testTimes; i++) {
            int size = RandomUtil.randomJust(maxSize);
            Queue<Integer> queue = new LinkedList<>();
            QueueFromArray queueFromArray = new QueueFromArray(size);
            List<Integer> errorRecord = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                int value = RandomUtil.random(maxValue);
                errorRecord.add(value);
                queue.offer(value);
                queueFromArray.offer(value);
            }
            System.out.println(Arrays.toString(queue.toArray()));
            System.out.println(Arrays.toString(queueFromArray.arr));
            for (int j = 0; j < size; j++) {
                Integer poll = queue.poll();
                Integer poll1 = queueFromArray.poll();
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

    int[] arr = null;
    int capacity = -1;
    int begin = -1;
    int end = -1;
    int size = -1;

    public QueueFromArray(int capacity) {
        this.capacity = capacity;
        this.arr = new int[capacity];
        size = 0;
    }

    /**
     * 放 从尾放
     *
     * @param value
     */
    public void offer(Integer value) {
        if (size <= capacity) {
            int position = getPosition(++end);
            arr[position] = value;
            size++;
        }

    }

    public Integer poll() {
        if (size > 0) {
            int position = getPosition(++begin);
            size--;
            return arr[position];
        }
        return null;
    }

    public int getPosition(int pos) {
        return pos >= capacity ? pos % capacity : pos;
    }
}
