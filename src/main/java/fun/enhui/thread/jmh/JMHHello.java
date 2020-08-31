package fun.enhui.thread.jmh;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author 胡恩会
 * @Date 2020/8/31 22:01
 **/
public class JMHHello {
    static List<Integer> nums = new ArrayList<>();

    static {
        // 准备 1000 个随机数
        Random r = new Random();
        for (int i = 0; i < 1000; i++) {
            nums.add(1000000 + r.nextInt(1000000));
        }
    }

    /**
     * 普通 方式遍历
     *
     * @Author: 胡恩会
     * @Date: 2020/8/31 22:13
     * @return: void
     **/
    static void foreach() {
        nums.forEach(JMHHello::isPrime);
    }

    /**
     * 并行处理流 方式遍历
     *
     * @Author: 胡恩会
     * @Date: 2020/8/31 22:14
     * @return: void
     **/
    static void parallel() {
        nums.parallelStream().forEach(JMHHello::isPrime);
    }

    /**
     * 判断一个数是否是质数
     *
     * @param num:
     * @Author: 胡恩会
     * @Date: 2020/8/31 22:14
     * @return: boolean
     **/
    static boolean isPrime(int num) {
        for (int i = 2; i <= num / 2; i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }
}
