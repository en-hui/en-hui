package fun.enhui.data.structure.utils;

/**
 * @Author 胡恩会
 * @Date 2020/6/2 21:42
 **/
public class ArrayUtil {
    /**
     * 随机样本生成器
     *
     * @param maxSize:  最大长度
     * @param maxValue: 最大值
     * @Author: 胡恩会
     * @Date: 2020/6/2 13:22
     * @return: int[]
     **/
    public static int[] generateRandomArray(int maxSize, int maxValue) {
        int arr[] = new int[(int) ((maxSize + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) ((maxValue + 1) * Math.random());
        }
        return arr;
    }

    /**
     * 复制数组
     *
     * @param arr1:原数组
     * @Author: 胡恩会
     * @Date: 2020/6/2 13:26
     * @return: int[] 复制出来的数组
     **/
    public static int[] copyArray(int[] arr1) {
        if (arr1 == null) {
            return null;
        }
        int[] res = new int[arr1.length];
        for (int i = 0; i < arr1.length; i++) {
            res[i] = arr1[i];
        }
        return res;
    }

    /**
     * 判断两个数组是否相同
     *
     * @param arr1:
     * @param arr2:
     * @Author: 胡恩会
     * @Date: 2020/6/2 20:53
     * @return: boolean
     **/
    public static boolean isEqual(int[] arr1, int[] arr2) {
        if ((arr1 == null && arr2 != null) || (arr1 != null && arr2 == null)) {
            return false;
        }
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 打印数组
     *
     * @param arr1:
     * @Author: 胡恩会
     * @Date: 2020/6/2 20:53
     * @return: void
     **/
    public static void printArray(int[] arr1) {
        for (int i = 0; i < arr1.length; i++) {
            System.out.print(arr1[i] + "  ");
        }
        System.out.println();
    }

}
