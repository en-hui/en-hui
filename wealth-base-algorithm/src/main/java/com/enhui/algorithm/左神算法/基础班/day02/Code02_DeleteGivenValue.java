package com.enhui.algorithm.左神算法.基础班.day02;

/**
 * 删除链表中给定值的节点
 *
 * @author 胡恩会
 * @date 2020/12/30 23:48
 */
public class Code02_DeleteGivenValue {
    public static class Node {
        public int value;

        public Node next;

        public Node(int value) {
            this.value = value;
        }
    }

    /**
     * 删除链表中所有数字为num的节点
     * 有可能会删除头节点，所以要将新的头节点返回
     * 1->4->5->3->5->1->2  删除1
     * 思路：
     * 1.先遍历链表，找到第一个不是1的节点作为新的头节点
     * 2.用两个新的指针分别指向前一个和当前节点，判断当前节点是否删除
     * 删除当前节点，就让上一个节点指向下一个节点
     * 不删除的话，上一节点和当前节点都往后挪位置
     *
     *
     * @param head 头节点
     * @param num  要删除的值
     * @return fun.enhui.左神算法.基础班.day02.Code01_ReverseList.Node
     * @author 胡恩会
     * @date 2020/12/30 23:47
     **/
    public static Node removeValue(Node head, int num) {
        // 遍历链表找到第一个不是1的位置
        while (head != null) {
            if (head.value != num) {
                break;
            }
            head = head.next;
        }
        // 记录上一个不是1的节点
        Node pre = head;
        // 记录当前节点
        Node cur = head;

        while (cur != null) {
            // 如果当前节点值为1，删除当前节点。即前一节点指向下一节点
            if (cur.value == num) {
                pre.next = cur.next;
            }
            // 不删除的话，往后挪位置
            pre = cur;
            cur = cur.next;
        }
        return head;
    }

    public static void main(String[] args) {
        Node node1 = new Node(1);
        Node node2 = new Node(4);
        Node node3 = new Node(5);
        Node node4 = new Node(3);
        Node node5 = new Node(5);
        Node node6 = new Node(1);
        Node node7 = new Node(2);

        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        node5.next = node6;
        node6.next = node7;

        Node head = removeValue(node1, 1);
        while (head != null) {
            System.out.println(head.value);
            head = head.next;
        }
    }
}
