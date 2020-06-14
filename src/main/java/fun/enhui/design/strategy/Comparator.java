package fun.enhui.design.strategy;

/**
 * 抽象比较策略接口，所有想指定比较器的类实现此接口，并重写比较方法
 *
 * @Author 胡恩会
 * @Date 2020/5/24 23:17
 **/
public interface Comparator<T> {
    int compare(T o1, T o2);
}
