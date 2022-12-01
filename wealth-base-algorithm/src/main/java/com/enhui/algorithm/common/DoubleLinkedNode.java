package com.enhui.algorithm.common;

import lombok.Data;

/**
 * 双链表数据结构
 */
public class DoubleLinkedNode {
    public DoubleLinkedNode last;
    public DoubleLinkedNode next;
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public DoubleLinkedNode(int value) {
        this.value = value;
    }

    /**
     * 比较两个双链表
     *
     * @param ans1
     * @param ans2
     * @return
     */
    public static boolean comparyLinked(DoubleLinkedNode ans1, DoubleLinkedNode ans2) {
        while (ans1 != null && ans2 != null) {
            if (ans1.getValue() == ans2.getValue()
                    && ((ans1.last == null && ans2.last == null) ||
                    (ans1.last != null && ans2.last != null && ans1.last.getValue() == ans2.last.getValue()))) {
                ans1 = ans1.next;
                ans2 = ans2.next;
            } else {
                return false;
            }
        }
        if (ans1 == null && ans2 == null) {
            return true;
        }
        return false;
    }

    /**
     * 打印双链表
     *
     * @param head
     */
    public static void printLinked(DoubleLinkedNode head) {
        while (head != null) {
            System.out.print(head.getValue() + " ");
            head = head.next;
        }
        System.out.println();
    }

    public static void printRevertLinked(DoubleLinkedNode tail) {
        while (tail != null) {
            System.out.print(tail.getValue() + " ");
            tail = tail.last;
        }
        System.out.println();
    }

    /**
     * 生成随机链表
     */
    public static DoubleLinkedNode generateRandomLinked(int maxSize, int maxValue) {
        int size = RandomUtil.randomJust(maxSize);
        int headValue = RandomUtil.random(maxValue);
        DoubleLinkedNode head = new DoubleLinkedNode(headValue);
        DoubleLinkedNode pre = null;
        DoubleLinkedNode curr = head;
        for (int i = 0; i < size - 1; i++) {
            int newValue = RandomUtil.random(maxValue);
            DoubleLinkedNode newNode = new DoubleLinkedNode(newValue);
            curr.next = newNode;
            curr.last = pre;
            pre = curr;
            curr = newNode;
        }
        return head;
    }
}
