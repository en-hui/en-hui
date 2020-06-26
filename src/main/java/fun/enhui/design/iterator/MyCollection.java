package fun.enhui.design.iterator;

/**
 * 模仿jdk的Collection
 *
 * @Author 胡恩会
 * @Date 2020/6/25 12:00
 **/
public interface MyCollection {
    void add(Object o);

    int size();

    MyIterator iterator();
}
