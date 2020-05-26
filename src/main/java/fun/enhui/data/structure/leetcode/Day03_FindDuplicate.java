package fun.enhui.data.structure.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * 给定一个包含 n + 1 个整数的数组 nums，其数字都在 1 到 n 之间（包括 1 和 n），
 * 可知至少存在一个重复的整数。假设只有一个重复的整数，找出这个重复的数。
 * 说明：
 * 不能更改原数组（假设数组是只读的）。
 * 只能使用额外的 O(1) 的空间。
 * 时间复杂度小于 O(n2) 。
 * 数组中只有一个重复的数字，但它可能不止重复出现一次。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/find-the-duplicate-number
 *
 * @Author 胡恩会
 * @Date 2020/5/26 19:58
 **/
public class Day03_FindDuplicate {

    public int findDuplicate(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.keySet().contains(nums[i])) {
                return nums[i];
            } else {
                map.put(nums[i], 1);
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] nums = {1, 3, 4, 2, 2};
        Day03_FindDuplicate instance = new Day03_FindDuplicate();
        int result = instance.findDuplicate(nums);
        System.out.println(result);
    }
}
