package fun.enhui.algorithm.base.d_linkedlist;

/**
 * 反转单链表
 *
 * @author 胡恩会
 * @date 2020/11/16 0:15
 */
public class ReverseSingleList {
    public static void main(String[] args) {
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        Node node = reverseLindList(node1);
        while (node != null) {
            System.out.println(node.value);
            node = node.next;
        }
    }

    /**
     * 反转单链表
     *
     * @Author: 胡恩会
     * @Date: 2020/6/7 21:34
     **/
    public static Node reverseLindList(Node head) {
        Node pre = null;
        Node next = null;
        while (head != null) {
            next = head.next;

            head.next = pre;
            pre = head;

            head = next;
        }
        return pre;
    }

    /**
     * 单链表的节点
     *
     * @Author: 胡恩会
     * @Date: 2020/6/7 21:35
     **/
    public static class Node {
        public int value;

        public Node next;

        public Node(int value) {
            this.value = value;
        }
    }
}
