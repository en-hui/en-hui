package fun.enhui.左神算法.基础班.day01;

/**
 * 二分 -- 在有序数组中找一个数字是否存在。
 *
 * @author 胡恩会
 * @date 2021/1/1 12:39
 */
public class Code04_BSExist {

    /**
     * 有序数组中找6
     * 数据 1 2 3 4 5 6 7 8 9
     * 下标 0 1 2 3 4 5 6 7 8
     * 1.L=0位置，R=8位置，mid=4位置，值是5-->5小于6，则向右找，把L指向5位置，则mid=6 （此处不是真正的中间位置了）
     * 2.L=5位置，R=8位置，mid=6位置，值是7-->7大于6，则向左找，把R指向5
     * 3.L=5位置，R=5位置，mid=5位置，值是6-->6等于6，找到了
     **/
    public static boolean exist(int[] sortedArr, int num) {
        if (sortedArr == null || sortedArr.length == 0) {
            return false;
        }
        int count = 0;
        int L = 0;
        int R = sortedArr.length - 1;
        int mid = 0;
        while (L < R) {
            count++;
            // mid = (L + R) / 2;
            mid = L + ((R - L) >> 1);
            if (sortedArr[mid] == num) {
                return true;
            } else if (sortedArr[mid] > num) {
                R = mid - 1;
            } else {
                L = mid + 1;
            }
        }
        System.out.println("查找次数：" + count);
        // 当L=R时未判断，此处补一次
        return sortedArr[L] == num;
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        boolean exist = exist(arr, 6);
        String result = exist ? "找到了!" : "没有这个数";
        System.out.println(result);
    }
}
