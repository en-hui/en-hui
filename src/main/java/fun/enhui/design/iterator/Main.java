package fun.enhui.design.iterator;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @Author 胡恩会
 * @Date 2020/6/25 11:49
 **/
public class Main {
    public static void main(String[] args) {
        MyCollection collection = new MyArrayList();
        for (int i = 0; i < 15; i++) {
            collection.add("a"+i);
        }
        MyIterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            Object o = iterator.next();
            System.out.println(o);
        }
    }
}
