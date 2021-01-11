package fun.enhui.左神算法.基础班.day06;

import java.util.HashSet;

/**
 * 两个链表，第一个相交节点问题
 * <br/>
 * 给定两个可能有环也可能无环的单链表，头节点head1和head2.
 * 请实现一个函数，如果两个链表相交，请返回相交的第一个节点。
 * 如果不相交，返回null
 * 要求：如果两个链表长度之和为N，时间复杂度请达到O(N),额外空间复杂度请达到O(1).
 *
 * @author 胡恩会
 * @date 2021/1/9 20:23
 */
public class Code05_FindFirstIntersectNode {
    public static class Node {
        int value;

        Node next;

        public Node(int value) {
            this.value = value;
        }
    }

    /**
     * 给定两个可能有环也可能无环的单链表，头节点head1和head2
     * 返回第一个相交的节点
     **/
    public static Node getInteresectNode(Node head1, Node head2) {
        if (head1 == null || head2 == null) {
            return null;
        }
        Node loop1 = getLoopNode(head1);
        Node loop2 = getLoopNode(head2);
        if (loop1 == null && loop2 == null) {
            return noLoop(head1, head2);
        }
        if (loop1 != null && loop2 != null) {
            return bothLoop(head1, loop1, head2, loop2);
        }
        return null;
    }

    /**
     * 给定一个链表，返回第一个入环的节点
     * 无环返回null
     **/
    private static Node getLoopNode(Node head) {
        // 0,1,2个节点肯定无环
        if (head == null || head.next == null || head.next.next == null) {
            return null;
        }
        Node fast = head.next.next;
        Node slow = head.next;
        // 用快慢指针判断有没有环
        while (fast != slow) {
            if (fast.next == null || fast.next.next == null) {
                return null;
            }
            fast = fast.next.next;
            slow = slow.next;
        }
        // 相遇后，让快指针回到head重新走，每次走一步
        fast = head;
        while (fast != slow) {
            fast = fast.next;
            slow = slow.next;
        }
        return fast;
    }

    /**
     * 两个无环的链表，找到第一个相交的节点
     * 遍历链表1，记录长度为l1，指针停在最后一个节点
     * 遍历链表2，记录长度为l2，指针停在最后一个节点
     * 先判断两个尾节点是否相等，不相等说明不相交。
     * 如果相等说明相交，长链表先走，剩余和短链表一样长度的时候一起走。第一次相等的点就是第一个相交节点
     **/
    private static Node noLoop(Node head1, Node head2) {
        Node root1 = head1;
        Node root2 = head2;
        int l1 = 0, l2 = 0;
        while (root1.next != null) {
            l1++;
            root1 = root1.next;
        }
        while (root2.next != null) {
            l2++;
            root2 = root2.next;
        }
        // 两个尾巴是同一节点，说明有相交，开始找第一个相交节点
        if (root1 == root2) {
            // root1 记录长的链表
            root1 = l1 > l2 ? head1 : head2;
            root2 = root1 == head1 ? head2 : head1;
            int diff = Math.abs(l1 - l2);
            // 长链表先走，剩下和短链表一样的长度一起走
            while (diff != 0) {
                root1 = root1.next;
                diff--;
            }
            while (root1 != root2) {
                root1 = root1.next;
                root2 = root2.next;
            }
            return root1;
        }
        return null;
    }

    /**
     * 两个有环的链表，找到第一个相交的节点
     **/
    private static Node bothLoop(Node head1, Node loop1, Node head2, Node loop2) {
        Node root1 = null, root2 = null;
        if (loop1 == loop2) {
            root1 = head1;
            root2 = head2;
            int l1 = 0, l2 = 0;
            while (root1 != loop1) {
                l1++;
                root1 = root1.next;
            }
            while (root2 != loop2) {
                l2++;
                root2 = root2.next;
            }
            root1 = l1 > l2 ? head1 : head2;
            root2 = root1 == head1 ? head2 : head1;
            int n = Math.abs(l1 - l2);
            while (n != 0) {
                root1 = root1.next;
                n--;
            }
            while (root1 != root2) {
                root1 = root1.next;
                root2 = root2.next;
            }
            return root1;
        } else {
            root1 = loop1.next;
            while (root1 != loop1) {
                if (root1 == loop2) {
                    return loop1;
                }
                root1 = root1.next;
            }
            return null;
        }
    }

    public static void main(String[] args) {
        Node head = new Node(4);
        Node node1 = new Node(2);
        Node node2 = new Node(1);
        Node node3 = new Node(6);
        Node node4 = new Node(9);
        Node node5 = new Node(0);
        Node node6 = new Node(7);
        Node node7 = new Node(3);
        head.next = node1;
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        node5.next = node6;
        node6.next = node7;
        node7.next = node4;
        Node loop = isLoop(head);
        System.out.println(loop.value);

    }

    /**
     * 用set找到两个无环链表的相交节点
     **/
    private static Node noLoopSet(Node head1, Node head2) {
        HashSet set = new HashSet();
        Node root1 = head1;
        Node root2 = head2;
        while (root1 != null) {
            set.add(root1);
            root1 = root1.next;
        }
        while (root2 != null) {
            boolean contains = set.contains(root2);
            if (contains) {
                return root2;
            } else {
                root2 = root2.next;
            }
        }
        return null;
    }

    /**
     * 用hashSet判断链表是否有环
     **/
    private static Node isLoop(Node head) {
        HashSet set = new HashSet();
        Node root = head;
        while (root != null) {
            boolean isExist = set.contains(root);
            if (isExist) {
                return root;
            } else {
                set.add(root);
            }
            root = root.next;
        }
        return null;
    }
}
