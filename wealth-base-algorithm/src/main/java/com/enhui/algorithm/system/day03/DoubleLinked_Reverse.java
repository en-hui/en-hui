package com.enhui.algorithm.system.day03;

import com.enhui.algorithm.structure.DoubleLinkedNode;

import java.util.ArrayList;
import java.util.List;

import static com.enhui.algorithm.structure.DoubleLinkedNode.comparyLinked;
import static com.enhui.algorithm.structure.DoubleLinkedNode.generateRandomLinked;
import static com.enhui.algorithm.structure.DoubleLinkedNode.printLinked;

/**
 * 双链表的反转
 */
public class DoubleLinked_Reverse {

    public static void main(String[] args) {
        int testTimes = 10000;
        int maxSize = 10;
        int maxValue = 10;
        boolean success = true;
        for (int i = 0; i < testTimes; i++) {
            DoubleLinkedNode head1 = generateRandomLinked(maxSize, maxValue);
            printLinked(head1);
            DoubleLinkedNode ans2 = check(head1);
            DoubleLinkedNode ans1 = reverse(head1);
            printLinked(head1);
            printLinked(ans1);
            printLinked(ans2);
            System.out.println("-------");
            boolean unitSuccess = comparyLinked(ans1, ans2);
            if (!unitSuccess) {
                success = false;
                System.out.print("测试失败，失败数据：\n");
                printLinked(ans1);
                printLinked(ans2);
                break;
            }
        }
        if (success) {
            System.out.printf("算法正确，测试次数：「%s」\n", testTimes);
        }
    }

    /**
     * 练习coding，反转单链表
     * 1 -> 2 -> 3 -> 4 -> null
     * 4 -> 3 -> 2 -> 1 -> null
     */
    public static DoubleLinkedNode reverse(DoubleLinkedNode cur) {
        DoubleLinkedNode pre = null;
        DoubleLinkedNode next = null;
        while (cur != null) {
            next = cur.next;
            cur.last = next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }

    /**
     * 用容器辅助实现对数器
     */
    public static DoubleLinkedNode check(DoubleLinkedNode head) {
        if (head == null) {
            return null;
        }
        List<Integer> list = new ArrayList<>();
        while (head != null) {
            list.add(head.getValue());
            head = head.next;
        }
        DoubleLinkedNode newHead = new DoubleLinkedNode(list.get(list.size() - 1));
        DoubleLinkedNode curr = newHead;
        for (int i = list.size() - 2; i >= 0; i--) {
            DoubleLinkedNode node = new DoubleLinkedNode(list.get(i));
            curr.next = node;
            node.last = curr;
            curr = node;
        }
        return newHead;
    }
}
