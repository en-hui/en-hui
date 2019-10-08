package fun.enhui.data.structure;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * 使用LinkedList将两个顺序存储的链表，合并为一个链表，保持顺序存储
 * @Author HuEnhui
 * @Date 2019/9/29 23:15
 **/
public class LinkedListTwo2One {

    public static void main(String[] args) {
        // 初始化链表
        LinkedList<Integer> linked1 = new LinkedList();
        LinkedList<Integer> linked2 = new LinkedList();
        initLinked(linked1,5);
        initLinked(linked2,6);
        // 获取迭代器，判断是否有下一节点
        ListIterator linkedIt1 = linked1.listIterator(0);
        ListIterator linkedIt2 = linked2.listIterator(0);
        // 创建最终链表，用于存放结果
        LinkedList<Integer> finalLinked = new LinkedList();
        while(linkedIt1.hasNext() && linkedIt2.hasNext()) {
            // 删除第一个节点，并将节点加入新链表
            finalLinked.add((linked1.getFirst() < linked2.getFirst())?linked1.remove():linked2.remove());
        }
        // 展示结果
        for (Integer num : finalLinked) {
            System.out.print(num+",");
        }
    }


    /**
     *  初始化链表，根据n的奇偶性，赋初始值
     * @author: HuEnhui
     * @date: 2019/9/27 17:53
     * @param linkedList 空链表对象
     * @param n 要初始化的长度
     * @return: void
     */
    public static void initLinked(LinkedList<Integer> linkedList,int n) {
        int initNum = 1;
        if(n%2 == 0) {
            initNum = 0;
        }
        for(int i=0;i<n;i++) {
            linkedList.add(initNum);
            initNum += 2;
        }

    }
}
