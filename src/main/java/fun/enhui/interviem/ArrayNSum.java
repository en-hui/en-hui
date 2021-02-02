package fun.enhui.interviem;

/**
 * 数组中找到是否存在 连续k个数的和为 k
 *
 * @author 胡恩会
 * @date 2021/2/2 22:11
 */
public class ArrayNSum {

    public static void main(String[] args) {
        ArrayNSum one = new ArrayNSum();
        int[] nums = new int[] {1,1,1,2};
        System.out.println(one.checkSum(nums, 6));
    }

    /**
     * 给定一个整数数组 nums 和一个目标值 k，
     * 请实现一个方法判断 nums 中是否存在某个片段（即若干个相连元素）之和等于 k。
     * 要求时间复杂度为 O(n)
     *
     * @param nums 数组
     * @param k    目标和-数字
     * @return 是否存在
     * @author 胡恩会
     * @date 2021/2/2 22:12
     **/
    public boolean checkSum(int[] nums, int k) {
        // Write your code here.
        // 如果数组是空的，肯定没有
        if (nums == null || nums.length == 0) {
            return false;
        }
        int sum = 0;
        int left = 0, right = 0;
        // 一直遍历
        while (right <= nums.length) {
            // 左右指针指向一个位置
            if (left == right) {
                sum = nums[left];
                // 如果这个位置的一个数比k大，左右指针一起右移
                if (sum > k) {
                    sum -= nums[left];
                    left++;
                    right++;
                } else if (sum == k) {
                    // 如果这个位置的数恰好等于k，直接返回
                    return true;
                } else {
                    // 如果这个数比k小，则right右移，产生窗口
                    right++;
                    sum += nums[right];
                }
            } else {
                // 左右指针不指向同一位置，有窗口的情况
                if (sum < k) {
                    right++;
                    // 越界返回false
                    if (right >= nums.length) {
                        return false;
                    }
                    sum += nums[right];
                } else if (sum == k) {
                    return true;
                } else {
                    sum -= nums[left];
                    left++;
                }
            }
        }
        return false;
    }
}
