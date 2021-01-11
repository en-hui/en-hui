package fun.enhui.左神算法.基础班.day06;

import java.util.ArrayList;
import java.util.List;

/**
 * 将单向链表按某值划分成左边小，中间相等，右边大的形式
 *
 * @author 胡恩会
 * @date 2021/1/9 17:16
 */
public class Code03_LinkedListPatition {

    public static class Node {
        int value;

        Node next;

        public Node(int value) {
            this.value = value;
        }
    }

    /**
     * 把链表放进数组里，在数组上做patition
     * 1.遍历链表，放进arrayList
     * 2.用arrayList做patition
     * 3.把数组串成链表
     **/
    public static Node patition1(Node head, int num) {
        if (head == null || head.next == null) {
            return head;
        }
        List<Integer> array = new ArrayList<>();
        Node root = head;
        while (root != null) {
            array.add(root.value);
            root = root.next;
        }
        // patition-->荷兰旗
        int leftIndex = -1;
        int rightIndex = array.size();
        for (int i = 0; i < rightIndex; i++) {
            if (array.get(i) < num) {
                leftIndex = leftIndex + 1;
                swap(array, i, leftIndex);
            } else if (array.get(i) > num) {
                rightIndex = rightIndex - 1;
                swap(array, i, rightIndex);
                i--;
            }
        }
        root = head;
        for (int i = 0; i < array.size(); i++) {
            root.value = array.get(i);
            root = root.next;
        }
        return head;
    }

    /**
     * 分成小中大三部分，在把各部分串起来
     * 1.用六个指针，smallHead，smallTail，equalHead，equalTail，bigHead，bigTail
     * 2.遍历链表，根据比较放入三个区域
     * 3.最后根据小中大的顺序把三个小链表串起来
     **/
    public static Node patition2(Node head, int num) {
        Node smallHead = null, smallTail = null;
        Node equalHead = null, equalTail = null;
        Node bigHead = null, bigTail = null;
        Node next = null;
        while (head != null) {
            next = head.next;
            head.next = null;
            if (head.value < num) {
                if (smallHead == null) {
                    smallHead = head;
                    smallTail = head;
                } else {
                    smallTail.next = head;
                    smallTail = head;
                }
            } else if (head.value == num) {
                if (equalHead == null) {
                    equalHead = head;
                    equalTail = head;
                } else {
                    equalTail.next = head;
                    equalTail = head;
                }
            } else if (head.value > num) {
                if (bigHead == null) {
                    bigHead = head;
                    bigTail = head;
                } else {
                    bigTail.next = head;
                    bigTail = head;
                }
            }
            head = next;

        }

        // 小于区域尾巴连等于区域头，等于区域尾巴连大于区域头
        if (smallHead != null) {
            smallTail.next = equalHead;
            equalTail = equalTail == null ? smallTail : equalTail;
        }
        if (equalHead != null) {
            equalTail.next = bigHead;
        }
        return smallHead != null ? smallHead : (equalHead != null ? equalHead : bigHead);

    }

    public static void main(String[] args) {
        Node head = new Node(3);
        Node node1 = new Node(4);
        Node node2 = new Node(7);
        Node node3 = new Node(0);
        Node node4 = new Node(6);
        Node node5 = new Node(2);
        Node node6 = new Node(5);
        Node node7 = new Node(4);
        head.next = node1;
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        node5.next = node6;
        node6.next = node7;
        //
        patition1(head, 4);
        Node root = head;
        System.out.println("=======第一种方式==========");
        while (root != null) {
            System.out.print(root.value);
            root = root.next;
        }
        System.out.println();

        //
        patition2(head, 4);
        System.out.println("=======第二种方式==========");
        root = head;
        while (root != null) {
            System.out.print(root.value);
            root = root.next;
        }
    }

    public static void swap(List<Integer> arrayList, int i, int j) {
        Integer temp = arrayList.get(i);
        arrayList.set(i, arrayList.get(j));
        arrayList.set(j, temp);
    }
}
