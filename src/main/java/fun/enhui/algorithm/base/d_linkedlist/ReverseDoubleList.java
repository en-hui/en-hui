package fun.enhui.algorithm.base.d_linkedlist;

/**
 * 反转双向链表
 *
 * @author 胡恩会
 * @date 2020/11/16 0:16
 */
public class ReverseDoubleList {
    public static void main(String[] args) {
        DoubleNode node1 = new DoubleNode(1);
        DoubleNode node2 = new DoubleNode(2);
        DoubleNode node3 = new DoubleNode(3);
        DoubleNode node4 = new DoubleNode(4);
        node1.next = node2;
        node1.last = null;
        node2.next = node3;
        node2.last = node1;
        node3.next = node4;
        node3.last = node2;
        node4.next = null;
        node4.last = node3;
        DoubleNode node = reverseDoubleList(node1);
        while (node != null) {
            System.out.println(node.value);
            node = node.next;
        }
    }

    /**
     * 反转双向链表
     *
     * @Author: 胡恩会
     * @Date: 2020/6/7 21:34
     **/
    public static DoubleNode reverseDoubleList(DoubleNode head) {
        DoubleNode pre = null;
        DoubleNode next = null;
        while (head != null) {
            next = head.next;

            head.next = pre;
            head.last = next;
            pre = head;

            head = next;
        }
        return pre;
    }

    /**
     * 双向链表的节点
     *
     * @Author: 胡恩会
     * @Date: 2020/6/7 21:35
     **/
    public static class DoubleNode {
        public int value;

        public DoubleNode next;

        public DoubleNode last;

        public DoubleNode(int value) {
            this.value = value;
        }
    }
}
