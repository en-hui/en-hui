package fun.enhui.左神算法.基础班.day06;

/**
 * 快慢指针的应用
 *
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

    public static void main(String[] args) {
        Node head = new Node(1);
        Node node1 = new Node(2);
        Node node2 = new Node(3);
        Node node3 = new Node(4);
        Node node4 = new Node(5);
        Node node5 = new Node(6);
        head.next = node1;
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;

        Node root = head;
        System.out.print("初始链表为： ");
        while (root != null) {
            System.out.print(root.value + " --> ");
            root = root.next;
        }
        System.out.println(" ");
        Node midOrUpMidNode = midOrUpMidNode(head);
        System.out.println("中点或上中点为：" + midOrUpMidNode.value);
        Node midOrDownMidNode = midOrDownMidNode(head);
        System.out.println("中点或下中点为：" + midOrDownMidNode.value);
        Node midOrUpMidPreNode = midOrUpMidPreNode(head);
        System.out.println("中点或上中点的前一个为：" + midOrUpMidPreNode.value);
        Node midOrDownPreNode = midOrDownPreNode(head);
        System.out.println("中点或下中点的前一个为：" + midOrDownPreNode.value);
    }
}
