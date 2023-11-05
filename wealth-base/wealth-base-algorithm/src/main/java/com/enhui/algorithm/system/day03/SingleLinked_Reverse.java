package com.enhui.algorithm.system.day03;

import com.enhui.algorithm.structure.SingleLinkedNode;

import java.util.ArrayList;
import java.util.List;

import static com.enhui.algorithm.structure.SingleLinkedNode.comparyLinked;
import static com.enhui.algorithm.structure.SingleLinkedNode.generateRandomLinked;
import static com.enhui.algorithm.structure.SingleLinkedNode.printLinked;

/**
 * 单链表的反转
 */
public class SingleLinked_Reverse {
    public static void main(String[] args) {
        int testTimes = 10000;
        int maxSize = 10;
        int maxValue = 10;
        boolean success = true;
        for (int i = 0; i < testTimes; i++) {
            SingleLinkedNode head1 = generateRandomLinked(maxSize, maxValue);
            printLinked(head1);
            SingleLinkedNode ans2 = check(head1);
            SingleLinkedNode ans1 = reverse(head1);
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
    public static SingleLinkedNode reverse(SingleLinkedNode cur) {
        SingleLinkedNode pre = null;
        SingleLinkedNode next = null;
        while (cur != null) {
            next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }

    /**
     * 用容器辅助实现对数器
     */
    public static SingleLinkedNode check(SingleLinkedNode head) {
        if (head == null) {
            return null;
        }
        List<Integer> list = new ArrayList<>();
        while (head != null) {
            list.add(head.getValue());
            head = head.next;
        }
        SingleLinkedNode newHead = new SingleLinkedNode(list.get(list.size() - 1));
        SingleLinkedNode curr = newHead;
        for (int i = list.size() - 2; i >= 0; i--) {
            SingleLinkedNode node = new SingleLinkedNode(list.get(i));
            curr.next = node;
            curr = node;
        }
        return newHead;
    }


}
