package com.enhui.design.prototype.deepcopy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 通过重写clone实现深拷贝
 * @author: HuEnhui
 * @date: 2019/12/23 9:25
 */
public class DeepCopyByClone {
    public static void main(String[] args) {
        SheepTarget sheepTarget = new SheepTarget("内对象",2);
        Sheep sheep = new Sheep("外对象",sheepTarget);
        Sheep sheep1 = (Sheep) sheep.clone();

        System.out.println(sheep);
        System.out.println(sheep1);
    }
}


/**
 * 重写clone实现深拷贝
 * （外部对象，包含一个引用类型成员变量）
 * @author: HuEnhui
 * @date: 2019/12/23 9:25
 */
@ToString
@Setter
@Getter
@AllArgsConstructor
class Sheep implements Cloneable{
    private String name;
    private SheepTarget sheepTarget;

    @Override
    protected Object clone() {
        Object deep = null;
        Sheep sheep = null;
        try {
            deep = super.clone();
            // 对引用类型单独处理
            sheep = (Sheep)deep;
            sheep.sheepTarget = (SheepTarget) sheepTarget.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return sheep;
    }
}

/**
 * 使用默认clone实现浅拷贝
 * （内部对象）
 * @author: HuEnhui
 * @date: 2019/12/23 9:56
 */
@Setter
@Getter
@AllArgsConstructor
class SheepTarget implements Cloneable{
    private String name;
    private int age;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
