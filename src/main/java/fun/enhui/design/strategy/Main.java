package fun.enhui.design.strategy;


import java.util.Arrays;

/**
 * 测试入口
 *
 * @Author 胡恩会
 * @Date 2020/5/24 22:57
 **/
public class Main {
    public static void main(String[] args) {

        Cat[] arr = {new Cat(1, 1), new Cat(9, 9), new Cat(5, 5)};
        Sort<Cat> sort = new Sort<Cat>();
        // 猫的身高比较策略
        sort.sort(arr, new CatHeightComparator());
        System.out.println(Arrays.toString(arr));
        // 猫的体重比较策略
        sort.sort(arr, new CatWeightComparator());
        System.out.println(Arrays.toString(arr));
    }
}
