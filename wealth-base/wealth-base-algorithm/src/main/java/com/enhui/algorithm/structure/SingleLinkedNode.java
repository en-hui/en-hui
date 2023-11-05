package com.enhui.algorithm.structure;

import com.enhui.algorithm.util.RandomUtil;

/**
 * 单链表数据结构
 */
public class SingleLinkedNode {
    public SingleLinkedNode next;
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public SingleLinkedNode(int value) {
        this.value = value;
    }

    /**
     * 打印单链表
     *
     * @param head
     */
    public static void printLinked(SingleLinkedNode head) {
        while (head != null) {
            System.out.print(head.getValue() + " -> ");
            head = head.next;
        }
        System.out.println();
    }

    /**
     * 生成随机链表
     */
    public static SingleLinkedNode generateRandomLinked(int maxSize, int maxValue) {
        int size = RandomUtil.randomJust(maxSize);
        int headValue = RandomUtil.random(maxValue);
        SingleLinkedNode head = new SingleLinkedNode(headValue);
        SingleLinkedNode curr = head;
        for (int i = 0; i < size - 1; i++) {
            int newValue = RandomUtil.random(maxValue);
            SingleLinkedNode newNode = new SingleLinkedNode(newValue);
            curr.next = newNode;
            curr = newNode;
        }
        return head;
    }

    /**
     * 复制一个单链表
     *
     * @param head
     * @return
     */
    public static SingleLinkedNode copyLinked(SingleLinkedNode head) {
        if (head == null) {
            return null;
        }
        SingleLinkedNode resHead = new SingleLinkedNode(head.getValue());
        SingleLinkedNode curr = resHead;
        while (head.next != null) {
            SingleLinkedNode next = head.next;
            SingleLinkedNode newNode = new SingleLinkedNode(next.getValue());
            curr.next = newNode;
            curr = newNode;
            head = next;
        }
        return resHead;
    }

    /**
     * 比较两个单链表
     *
     * @param ans1
     * @param ans2
     * @return
     */
    public static boolean comparyLinked(SingleLinkedNode ans1, SingleLinkedNode ans2) {
        while (ans1 != null && ans2 != null) {
            if (ans1.getValue() == ans2.getValue()) {
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
}
