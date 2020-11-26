package fun.enhui.algorithm.base.d_linkedlist;

/**
 * 删除链表中给定值
 *
 * @author 胡恩会
 * @date 2020/11/16 0:17
 */
public class RemoveNode {
    public static void main(String[] args) {
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        Node node = removeValue(node1, 1);
        while (node != null) {
            System.out.println(node.value);
            node = node.next;
        }
    }

    /**
     * 链表中删除给定值
     *
     * @param head:
     * @param value:
     * @Author: 胡恩会
     * @Date: 2020/6/7 22:34
     * @return: Node
     **/
    public static Node removeValue(Node head, int value) {
        // 将头指到第一个不为value的位置
        while (true) {
            if (head.value != value) {
                break;
            } else {
                head = head.next;
            }
        }
        Node current = head.next;
        Node pre = head;
        while (current != null) {
            if (current.value == value) {
                pre.next = current.next;
            } else {
                pre = current;
            }
            current = current.next;
        }
        return head;
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
