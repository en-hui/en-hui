package com.enhui.algorithm.system.day03;

import com.enhui.algorithm.common.RandomUtil;
import com.enhui.algorithm.common.SingleLinkedNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 单链表的反转
 */
public class SingleLinked_Reverse {
    public static void main(String[] args) {
        int testTimes = 1;
        int maxSize = 10;
        int maxValue = 10;
        for (int i = 0; i < testTimes; i++) {
            SingleLinkedNode head1 = generateRandomLinked(maxSize, maxValue);
            SingleLinkedNode head2 = copyLinked(head1);
            printLinked(head1);
            printLinked(head2);
            SingleLinkedNode ans2 = check(head2);
            printLinked(ans2);
        }
    }

    private static SingleLinkedNode copyLinked(SingleLinkedNode head) {
        if (head == null) {
            return null;
        }
        SingleLinkedNode resHead = new SingleLinkedNode(head.getValue());
        SingleLinkedNode curr = resHead;
        while (head.getNext() != null) {
            SingleLinkedNode next = head.getNext();
            SingleLinkedNode newNode = new SingleLinkedNode(next.getValue());
            curr.setNext(newNode);
            curr = newNode;
            head = next;
        }
        return resHead;
    }

    private static void printLinked(SingleLinkedNode head) {
        while (head != null) {
            System.out.print(head.getValue() + " ");
            head = head.getNext();
        }
        System.out.println();
    }

    /**
     * 练习coding，反转单链表
     */
    public static SingleLinkedNode reverse(SingleLinkedNode head) {

        return head;
    }

    /**
     * 用容器辅助实现对数器
     */
    public static SingleLinkedNode check(SingleLinkedNode head) {
        List<Integer> list = new ArrayList<>();
        while (head != null) {
            list.add(head.getValue());
            head = head.getNext();
        }
        SingleLinkedNode newHead = new SingleLinkedNode(list.get(list.size() - 1));
        SingleLinkedNode curr = newHead;
        for (int i = list.size() - 2; i >= 0; i--) {
            SingleLinkedNode node = new SingleLinkedNode(list.get(i));
            curr.setNext(node);
            curr = node;
        }
        return newHead;
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
            curr.setNext(newNode);
            curr = newNode;
        }
        return head;
    }
}
