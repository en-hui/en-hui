package fun.enhui.design.strategy;

import java.util.Comparator;

/**
 * 猫类
 *
 * @Author 胡恩会
 * @Date 2020/5/24 23:09
 **/
public class Cat {
    int height;
    int weight;

    public Cat(int height, int weight) {
        this.height = height;
        this.weight = weight;
    }


    @Override
    public String toString() {
        return "Cat{" +
                "height=" + height +
                ", weight=" + weight +
                '}';
    }
}
