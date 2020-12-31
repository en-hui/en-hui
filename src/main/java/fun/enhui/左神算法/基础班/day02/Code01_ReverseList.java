package fun.enhui.左神算法.基础班.day02;

/**
 * 反转链表
 * 1.单向链表的反转
 * 2.双向链表的反转
 *
 * @author 胡恩会
 * @date 2020/12/30 23:44
 */
public class Code01_ReverseList {

    public static class SinlgNode {
        public int value;

        public SinlgNode next;

        public SinlgNode(int value) {
            this.value = value;
        }
    }

    public static class DoubleNode {
        public int value;

        public DoubleNode last;

        public DoubleNode next;

        public DoubleNode(int value) {
            this.value = value;
        }
    }

    /**
     * 反转单向链表
     * 1->2->3->4->null
     * 反转后的结果
     * 4->3->2->1->null
     * 思路：使用pre、head、next记录前一个、当前、后一个节点
     * 以三个为一组考虑，
     *
     * @param head 头节点
     * @return fun.enhui.左神算法.基础班.day02.Code01_ReverseList.SinlgNode
     * @author 胡恩会
     * @date 2020/12/31 0:24
     **/
    public static SinlgNode reverseSinlgList(SinlgNode head) {
        SinlgNode pre = null;
        SinlgNode next = null;
        while(head != null) {
            // 暂存下一个节点
            next = head.next;
            // 改变当前节点指向
            head.next = pre;
            // 节点后挪
            pre = head;
            head = next;
        }

        return pre;
    }

    /**
     * 反转双向链表
     * null<-1<->2<->3<->4->null
     * 反转后的结果
     * null<-4<->3<->2<-1->null
     *
     * @param head 头节点
     * @return fun.enhui.左神算法.基础班.day02.Code01_ReverseList.DoubleNode
     * @author 胡恩会
     * @date 2020/12/31 0:24
     **/
    public static DoubleNode reverseDoubleList(DoubleNode head) {
        DoubleNode pre = null;
        DoubleNode next = null;
        while (head != null) {
            // 暂存下一个节点
            next = head.next;
            // 改变当前节点指向
            head.next = pre;
            head.last = next;
            // 节点后挪
            pre = head;
            head = next;
        }
        return pre;
    }

    public static void main(String[] args) {
        SinlgNode node1 = new SinlgNode(1);
        SinlgNode node2 = new SinlgNode(2);
        SinlgNode node3 = new SinlgNode(3);
        SinlgNode node4 = new SinlgNode(4);
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        SinlgNode head = reverseSinlgList(node1);
        System.out.println("---------单向链表反转-----------");
        while (head != null) {
            System.out.println(head.value);
            head = head.next;
        }

        DoubleNode node11 = new DoubleNode(1);
        DoubleNode node22 = new DoubleNode(2);
        DoubleNode node33 = new DoubleNode(3);
        DoubleNode node44 = new DoubleNode(4);
        node11.next = node22;
        node22.last = node11;
        node22.next = node33;
        node33.last = node22;
        node33.next = node44;
        node44.last = node33;
        DoubleNode doubleNode = reverseDoubleList(node11);
        System.out.println("---------双向链表反转-----------");
        while (doubleNode != null) {
            System.out.println(doubleNode.value);
            doubleNode = doubleNode.next;
        }
    }
}
