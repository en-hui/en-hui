package com.enhui.algorithm.system.day03;

import com.enhui.algorithm.structure.DoubleLinkedNode;
import com.enhui.algorithm.util.RandomUtil;

import java.util.ArrayList;

import static com.enhui.algorithm.structure.DoubleLinkedNode.comparyLinked;
import static com.enhui.algorithm.structure.DoubleLinkedNode.generateRandomLinked;
import static com.enhui.algorithm.structure.DoubleLinkedNode.printLinked;

/**
 * 双链表把给定值都删除
 */
public class DoubleLinked_DeleteNum {
    public static void main(String[] args) {
        int testTimes = 10000;
        int maxSize = 10;
        int maxValue = 3;
        boolean success = true;
        for (int i = 0; i < testTimes; i++) {
            DoubleLinkedNode head1 = generateRandomLinked(maxSize, maxValue);
            int num = RandomUtil.random(maxValue);
            printLinked(head1);
            System.out.println(num);
            DoubleLinkedNode ans2 = check(head1, num);
            DoubleLinkedNode ans1 = deleteNum(head1, num);
            System.out.println("-------");
            boolean unitSuccess = comparyLinked(ans1, ans2);
            if (!unitSuccess) {
                success = false;
                System.out.printf("测试失败，失败数据：num:{%s} \n", num);
                printLinked(ans1);
                printLinked(ans2);
                break;
            }
        }
        if (success) {
            System.out.printf("算法正确，测试次数：「%s」\n", testTimes);
        }
    }

    private static DoubleLinkedNode deleteNum(DoubleLinkedNode curr, int num) {
        while (curr != null && curr.getValue() == num) {
            curr = curr.next;
        }
        if (curr == null) {
            return curr;
        }
        curr.last = null;
        DoubleLinkedNode newHead = curr;
        DoubleLinkedNode pre = curr;
        curr = curr.next;
        while (curr != null) {
            if (curr.getValue() != num) {
                pre.next = curr;
                curr.last = pre;
                pre = curr;
            }
            curr = curr.next;
        }
        pre.next = null;
        return newHead;
    }

    private static DoubleLinkedNode check(DoubleLinkedNode head1, int num) {
        if (head1 == null) {
            return head1;
        }
        ArrayList<Integer> list = new ArrayList<>();
        while (head1 != null) {
            if (head1.getValue() != num) {
                list.add(head1.getValue());
            }
            head1 = head1.next;
        }
        int size = list.size();
        if (size == 0) {
            return null;
        }
        DoubleLinkedNode head = new DoubleLinkedNode(list.get(0));
        DoubleLinkedNode curr = head;
        for (int i = 1; i < size; i++) {
            DoubleLinkedNode node = new DoubleLinkedNode(list.get(i));
            curr.next = node;
            node.last = curr;
            curr = curr.next;
        }
        return head;
    }
}
