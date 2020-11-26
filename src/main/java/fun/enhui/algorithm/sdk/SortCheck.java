package fun.enhui.algorithm.sdk;

import java.util.Arrays;

/**
 * 排序的对数器
 * 使用方法：调用SortCheck.check()方法，将排序类对象传进来即可
 *
 * @author 胡恩会
 * @date 2020/11/15 22:19
 **/
public class SortCheck {
    /**
     * 测试次数
     **/
    private static final int TEST_TIME = 500000;

    /**
     * 数组最大长度
     **/
    private static final int MAX_SIZE = 100;

    /**
     * 最大值
     **/
    private static final int MAX_VALUE = 100;

    /**
     * 校验排序算法是否正确
     *
     * @param sort 排序算法
     * @return boolean 算法是否正确
     * @author 胡恩会
     * @date 2020/11/15 22:06
     **/
    public static boolean check(BaseSort sort) {

        boolean succed = true;
        for (int i = 0; i < TEST_TIME; i++) {
            // 生成两组相同的数据源进行排序，假设结果相同，则认定为排序算法正确
            int[] arr1 = ArrayUtil.generateRandomArray(MAX_SIZE, MAX_VALUE);
            int[] arr2 = ArrayUtil.copyArray(arr1);
            sort.sort(arr1);
            systemSort(arr2);
            if (!ArrayUtil.isEqual(arr1, arr2)) {
                succed = false;
                ArrayUtil.printArray(arr1);
                ArrayUtil.printArray(arr2);
                break;
            }
        }
        return succed;
    }

    /**
     * 使用系统的排序算法——一个肯定正确的算法
     *
     * @param arr 数组
     * @author 胡恩会
     * @date 2020/11/15 22:17
     **/
    private static void systemSort(int[] arr) {
        Arrays.sort(arr);
    }

}
