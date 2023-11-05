package com.enhui.design.strategy;

/**
 * 猫的体重比较策略
 *
 * @Author 胡恩会
 * @Date 2020/5/24 23:23
 **/
public class CatWeightComparator implements Comparator<Cat> {
    public int compare(Cat o1, Cat o2) {
        if (o1.weight > o2.weight) return 1;
        else if (o1.weight < o2.weight) return -1;
        else return 0;
    }
}
