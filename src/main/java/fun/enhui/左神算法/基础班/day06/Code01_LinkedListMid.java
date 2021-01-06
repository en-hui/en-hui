package fun.enhui.左神算法.基础班.day06;

/**
 * @author 胡恩会
 * @date 2021/1/6 21:36
 */
public class Code01_LinkedListMid {
    public static class Node {
        public int value;

        public Node next;

        public Node(int v) {
            this.value = v;
        }
    }

    /**
     * 返回中点或上中点
     * 链表长度是奇数，返回中点
     * 链表长度是偶数，返回上中点
     **/
    public static Node midOrUpMidNode(Node head) {
        if (head == null || head.next == null || head.next.next == null) {
            return head;
        }
        // 链表必有三个或三个以上节点
        Node slow = head.next;
        Node fast = head.next.next;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    /**
     * 返回中点或下中点
     * 链表长度是奇数，返回中点
     * 链表长度是偶数，返回下中点
     **/
    public static Node midOrDownMidNode(Node head) {
        if (head == null || head.next == null) {
            return head;
        }
        Node slow = head.next;
        Node fast = head.next;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    /**
     * 返回中点的前一个或上中点的前一个
     * 链表长度是奇数，返回中点的前一个节点
     * 链表长度是偶数，返回上中点的前一个节点
     **/
    public static Node midOrUpMidPreNode(Node head) {
        if (head == null || head.next == null || head.next.next == null) {
            return null;
        }
        Node slow = head;
        Node fast = head.next.next;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    /**
     * 返回中点的前一个或下中点的前一个
     * 链表长度是奇数，返回中点的前一个节点
     * 链表长度是偶数，返回下中点的前一个节点
     **/
    public static Node midOrDownPreNode(Node head) {
        if (head == null || head.next == null) {
            return null;
        }
        if (head.next.next == null) {
            return head;
        }
        Node slow = head;
        Node fast = head.next;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }
}
