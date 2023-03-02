package com.enhui.algorithm.system.day03;

import com.enhui.algorithm.structure.DoubleLinkedNode;
import com.enhui.algorithm.util.RandomUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

import static com.enhui.algorithm.structure.DoubleLinkedNode.printRevertLinked;

/**
 * 双向链表实现队列
 */
public class QueueFromDoubleLinked {
    public static void main(String[] args) {
        int testTimes = 10000;
        int maxSize = 10;
        int maxValue = 10;
        boolean success = true;
        for (int i = 0; i < testTimes; i++) {
            Queue<Integer> queue = new LinkedList<>();
            QueueFromDoubleLinked queueFromDoubleLinked = new QueueFromDoubleLinked();
            int size = RandomUtil.randomJust(maxSize);
            List<Integer> errorRecord = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                int value = RandomUtil.random(maxValue);
                errorRecord.add(value);
                queue.offer(value);
                queueFromDoubleLinked.offer(value);
            }
            System.out.println(Arrays.toString(queue.toArray()));
            printRevertLinked(queueFromDoubleLinked.tail);

            for (int j = 0; j < size; j++) {
                Integer poll = queue.poll();
                Integer poll1 = queueFromDoubleLinked.poll();
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

    DoubleLinkedNode head;
    DoubleLinkedNode tail;

    public void offer(Integer value) {
        DoubleLinkedNode newNode = new DoubleLinkedNode(value);
        newNode.next = head;
        if (head != null) {
            head.last = newNode;
            head = newNode;
        } else {
            head = newNode;
            tail = newNode;
        }
    }

    public Integer poll() {
        if (tail == null) {
            return null;
        }
        int value = tail.getValue();
        DoubleLinkedNode pre = tail.last;
        if (pre == null) {
            head = null;
            tail = null;
        } else {
            pre.next = null;
            tail = pre;
        }
        return value;
    }
}
