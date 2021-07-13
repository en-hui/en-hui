package com.enhui.algorithm.左神算法.基础班.day06;

import java.util.Stack;

/**
 * 给定一个单链表的头节点head，请判断该链表是否为回文结构
 *
 * @author 胡恩会
 * @date 2021/1/9 14:31
 */
public class Code02_LinkedListIsPalindrome {
    public static class Node {
        int value;

        Node next;

        public Node(int value) {
            this.value = value;
        }
    }

    /**
     * 用栈判断
     * 1.将链表遍历向栈中存一份
     * 2.重新遍历链表，同时从栈中弹元素进行比较
     **/
    public static boolean isPalindrome1(Node head) {
        Stack<Integer> help = new Stack<>();
        // root 用来遍历链表
        Node root = head;
        while (root != null) {
            help.push(root.value);
            root = root.next;
        }

        root = head;
        while (root != null) {
            if (root.value == help.pop()) {
                root = root.next;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 在原有链表上做操作
     * 1.使用快慢指针找到链表的中点（找中点或上中点），将此节点记录mid
     * 2.将中点（mid.next）以后的链表进行反转。则有前半部分和后半部分链表，并拥有两个head
     * 3.遍历两个子链表进行比较（遍历至任何一个next为空）
     * 4.把后半部分反转回来,用mid指向反转回来的head
     **/
    public static boolean isPalindrome2(Node head) {
        boolean result = true;
        // 找中点或上中点
        Node mid = getMidOrUpMid(head);
        // 反转中点以后的链表，得到右头节点
        Node root = reverse(mid.next);
        mid.next = null;

        Node left = head;
        Node right = root;
        while (left != null && right != null) {
            if (left.value == right.value) {
                left = left.next;
                right = right.next;
            }else {
                result = false;
                break;
            }
        }
        // 后半部分反转回来
        mid.next = reverse(root);
        return result;
    }

    /**
     * 找中点或上中点
     * 1-->2-->3-->4-->5-->6-->
     * 0,1,2个节点，中点为head
     * 3,4个节点，中点为2
     * 5,6个节点，中点为3
     **/
    private static Node getMidOrUpMid(Node head) {
        if (head == null || head.next == null || head.next.next == null) {
            return head;
        }
        Node slow = head;
        Node fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    /**
     * 单链表反转
     **/
    private static Node reverse(Node head) {
        // 0，1 个节点，不需要反转
        if (head == null || head.next == null) {
            return head;
        }
        Node pre = null;
        Node cur = head;
        while (cur != null) {
            // 暂存一下后面的节点
            Node node = cur.next;
            cur.next = pre;

            pre = cur;
            cur = node;
        }
        return pre;
    }

    public static void main(String[] args) {
        Node head = new Node(1);
        Node node1 = new Node(2);
        Node node2 = new Node(3);
        Node node3 = new Node(2);
        Node node4 = new Node(1);
        Node node5 = new Node(1);
        head.next = node1;
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;

        Node root = head;
        while (root != null) {
            System.out.print(root.value + "-->");
            root = root.next;
        }
        System.out.println(" ");
        boolean palindrome1 = isPalindrome1(head);
        root = head;
        while (root != null) {
            System.out.print(root.value + "-->");
            root = root.next;
        }
        System.out.println("栈辅助判断--此链表是否为回文结构" + palindrome1);

        boolean palindrome2 = isPalindrome2(head);
        root = head;
        while (root != null) {
            System.out.print(root.value + "-->");
            root = root.next;
        }
        System.out.println("反转后半部分链表判断--此链表是否为回文结构" + palindrome2);
    }
}
