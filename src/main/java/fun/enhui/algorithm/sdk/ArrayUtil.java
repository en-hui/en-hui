package fun.enhui.algorithm.sdk;

/**
 * 数组的工具
 *
 * @author 胡恩会
 * @date 2020/11/15 23:52
 */
public class ArrayUtil {
    /**
     * 随机样本生成器
     *
     * @param maxSize  最大长度
     * @param maxValue 最大值
     * @return int[]
     * @author 胡恩会
     * @date 2020/11/15 22:18
     **/
    public static int[] generateRandomArray(int maxSize, int maxValue) {
        int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) ((maxValue + 1) * Math.random());
        }
        return arr;
    }

    /**
     * 复制数组
     *
     * @param arr1 原数组
     * @return int[] 复制出来的数组
     * @author 胡恩会
     * @date 2020/11/15 22:18
     **/
    public static int[] copyArray(int[] arr1) {
        if (arr1 == null) {
            return null;
        }
        int[] res = new int[arr1.length];
        System.arraycopy(arr1, 0, res, 0, arr1.length);
        return res;
    }

    /**
     * 判断两个数组是否相同
     *
     * @param arr1 数组1
     * @param arr2 数组2
     * @return boolean
     * @author 胡恩会
     * @date 2020/11/15 22:16
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
     * @param arr1 要打印的数组
     * @author 胡恩会
     * @date 2020/11/15 22:15
     **/
    public static void printArray(int[] arr1) {
        for (int j : arr1) {
            System.out.print(j + "  ");
        }
        System.out.println();
    }
}
