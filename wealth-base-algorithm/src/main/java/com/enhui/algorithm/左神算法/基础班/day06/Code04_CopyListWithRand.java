package com.enhui.algorithm.左神算法.基础班.day06;

import java.util.HashMap;

/**
 * 复制一个不太单纯的单向链表
 * 一种特殊的单链表节点类描述如下
 * class Node {
 * int value;
 * Node next;
 * Node rand;
 * Node(int val) { value = val;}
 * }
 * rand指针是单链表节点结构中新增的指针，rand可能指向链表中的任意一个节点，也可能指向null。
 * 给定一个由Node节点类型组成的无环单链表的头节点head，请实现一个函数完成这个链表的复制，并返回复制的新链表的头节点。
 * 要求：时间复杂度O(N),额外空间复杂度O(1)
 *
 * @author 胡恩会
 * @date 2021/1/9 19:17
 */
public class Code04_CopyListWithRand {

    public static class Node {
        int value;

        Node next;

        Node rand;

        public Node(int val) {
            value = val;
        }
    }

    /**
     * 1.先把所有节点都克隆，并用map存起来对应关系。Map<原节点，新节点>
     * 2.遍历原链表，对新链表的next和rand赋值
     * 3.返回原链表头对应的新链表头
     **/
    public static Node copyListWithRand1(Node head) {
        HashMap<Node, Node> map = new HashMap<>();
        Node cur = head;
        // 克隆所有节点放入map中
        while (cur != null) {
            map.put(cur, new Node(cur.value));
            cur = cur.next;
        }
        // 遍历原链表，对新链表的next和rand赋值
        cur = head;
        while (cur != null) {
            // cur 原节点
            // map.get(cur) 新节点
            map.get(cur).next = map.get(cur.next);
            map.get(cur).rand = map.get(cur.rand);
            cur = cur.next;
        }
        return map.get(head);
    }

    public static Node copyListWithRand2(Node head) {
        if (head == null) {
            return head;
        }
        Node cur = head;
        Node next = null;
        // 1.把克隆的节点挂在原节点后面
        while (cur != null) {
            // 暂存下一个节点
            next = cur.next;
            // 将1-->2 处理成为 1-->1'-->2
            cur.next = new Node(cur.value);
            cur.next.next = next;
            // 当前指针指到下一个原节点
            cur = next;
        }
        cur = head;
        Node curCopy = null;
        // 2.处理rand指针指向
        while (cur != null) {
            next = cur.next.next;
            curCopy = cur.next;
            curCopy.rand = (cur.rand != null) ? cur.rand.next : null;
            cur = next;
        }

        Node result = head.next;
        cur = head;
        // 分离链表，得到克隆的结果
        while (cur != null) {
            next = cur.next.next;
            curCopy = cur.next;
            // 两个链表重新指向next指针
            cur.next = next;
            curCopy.next = next != null ? next.next : null;
            cur = next;
        }
        return result;
    }

    public static void main(String[] args) {

    }
}
