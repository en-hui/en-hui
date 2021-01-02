package fun.enhui.左神算法.基础班.day03;

/**
 * 找出有几个降序对
 * {3,1,7,0,2}
 * 一个数组中，所有向右看比当前数小的数，都可以组成降序对
 * 此数组从3看  3，1   3，0  3，2
 * 从1看  1，0
 * 从7看 7，0  7，2
 * 从0看没有
 * 从2看没有
 * 结果：一共有6个降序对
 *
 * @author 胡恩会
 * @date 2021/1/2 19:54
 */
public class Code03_DescPair {

    public static void main(String[] args) {
        int[] arr = {3, 1, 7, 0, 2};

        int count = mergeSort(arr, 0, arr.length - 1);
        System.out.println(count);
    }

    private static int mergeSort(int[] arr, int L, int R) {
        if (L == R) {
            return 0;
        }
        int mid = L + ((R - L) >> 1);
        int leftCount = mergeSort(arr, L, mid);
        int rightCount = mergeSort(arr, mid + 1, R);

        return leftCount + rightCount + merge(arr, L, mid, R);
    }

    public static int merge(int[] arr, int L, int M, int R) {
        int count = 0;
        int[] help = new int[R - L + 1];
        int helpIndex = 0;
        int leftIndex = L;
        int rightIndex = M + 1;
        while (leftIndex <= M && rightIndex <= R) {
            // for example ：3,1   7,2,0
            count += (arr[leftIndex] > arr[rightIndex] ? R - rightIndex + 1 : 0);
            // 谁大谁先放 --> 排序出来是从大到小
            help[helpIndex++] = arr[leftIndex] > arr[rightIndex] ? arr[leftIndex++] : arr[rightIndex++];
        }

        while (leftIndex <= M) {
            help[helpIndex++] = arr[leftIndex++];
        }
        while (rightIndex <= R) {
            help[helpIndex++] = arr[rightIndex++];
        }

        for (int i = 0; i < help.length; i++) {
            arr[L + i] = help[i];
        }
        return count;
    }
}
