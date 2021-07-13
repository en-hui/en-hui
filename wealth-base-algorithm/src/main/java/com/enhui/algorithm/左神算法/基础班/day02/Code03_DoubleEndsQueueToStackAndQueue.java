package com.enhui.algorithm.左神算法.基础班.day02;

/**
 * 双向链表实现栈和队列
 *
 * @author 胡恩会
 * @date 2021/1/1 17:08
 */
public class Code03_DoubleEndsQueueToStackAndQueue {

    /**
     * 双向链表的节点对象
     **/
    public static class Node<T> {
        public T value;

        public Node<T> last;

        public Node<T> next;

        public Node(T value) {
            this.value = value;
        }
    }

    /**
     * 双向链表实现队列
     * 需要一个头指针和尾指针
     * 需要一个从头添加的方法和一个从尾取出的方法
     **/
    public static class DoubleEndsQueue<T> {
        Node<T> head;

        Node<T> tail;

        /**
         * 从头添加元素
         **/
        public void addFromHead(T value) {
            Node<T> cur = new Node<>(value);
            if (head == null) {
                head = cur;
                tail = cur;
            } else {
                cur.next = head;
                head.last = cur;
                head = cur;
            }
        }

        /**
         * 从尾部添加元素
         **/
        public void addFromBottom(T value) {
            Node<T> cur = new Node<>(value);
            if (tail == null) {
                head = cur;
                tail = cur;
            } else {
                cur.next = tail;
                tail.last = cur;
                tail = cur;
            }
        }

        /**
         * 从头部弹出节点
         **/
        public T popFromHead() {
            if (head == null) {
                return null;
            }
            Node<T> cur = head;
            // 只有一个节点
            if (head == tail) {
                head = null;
                tail = null;
            } else {
                head = head.next;
                head.last = null;
                cur.next = null;
            }
            return cur.value;
        }

        /**
         * 从尾部弹出节点
         **/
        public T popFromTail() {
            if (tail == null) {
                return null;
            }
            Node<T> cur = tail;
            if (head == tail) {
                head = null;
                tail = null;
            } else {
                tail = tail.last;
                tail.next = null;
                cur.last = null;
            }
            return cur.value;
        }

        public boolean isEmpty() {
            return head == null;
        }
    }

    /**
     * 我的栈
     * 从头部加，从头部取
     **/
    public static class MyStack<T> {
        private DoubleEndsQueue<T> queue;

        public MyStack() {
            queue = new DoubleEndsQueue<>();
        }

        public void push(T value) {
            queue.addFromHead(value);
        }

        public T pop() {
            return queue.popFromHead();
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }
    }

    /**
     * 我的队列
     * 从头部加，从尾部取
     **/
    public static class MyQueue<T> {
        private DoubleEndsQueue<T> queue;

        public MyQueue() {
            queue = new DoubleEndsQueue<>();
        }

        public void push(T value) {
            queue.addFromHead(value);
        }

        public T pop() {
            return queue.popFromTail();
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }
    }
}
