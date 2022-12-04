package com.enhui.algorithm.system.day05;

public class SortedMerge_CountRangeSum {

    public static void main(String[] args) {

    }

    public int countRangeSum(int[] nums, int lower, int upper) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int[] sum = new int[nums.length];
        sum[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            sum[i] = sum[i - 1] + nums[i];
        }
        return count(sum, 0, nums.length - 1, lower, upper);
    }

    /**
     * arr[L..R] 不传进来，只传sum[] 前缀和数组
     * 求在原始数组arr[L..R]中，有多少个子数组累加和在[lower,upper]
     */
    private int count(int[] sum, int L, int R, int lower, int upper) {
        if (L == R) {
            // sum(L..L) 等同于 arr(0..L)的前缀和
            return (lower <= sum[L] && sum[L] <= upper) ? 1 : 0;
        }
        int mid = L + ((R - L) >> 1);
        int leftCount = count(sum, L, mid, lower, upper);
        int rightCount = count(sum, mid + 1, R, lower, upper);
        int mergeCount = merge(sum, L, mid, R, lower, upper);
        return leftCount + rightCount + mergeCount;
    }

    private int merge(int[] sum, int L, int mid, int R, int lower, int upper) {
        // 不merge，对于右组中的每个数X，求左组中有多少个数，位于[X-upper，X-lower]范围
        int ans = 0;
        int windowL = L;
        int windowR = L;
        for (int i = mid + 1; i <= R; i++) {
            int min = sum[i] - upper;
            int max = sum[i] - lower;
            while (windowR <= mid && sum[windowR] <= max) {
                windowR++;
            }
            while (windowL <= mid && sum[windowL] < min) {
                windowL++;
            }
            ans += (windowR - windowL);
        }
        // merge 排序
        int[] res = new int[R - L + 1];
        int i = 0;
        int LI = L;
        int RI = mid + 1;
        while (LI <= mid && RI <= R) {
            res[i++] = sum[LI] <= sum[RI] ? sum[LI++] : sum[RI++];
        }
        while (LI <= mid) {
            res[i++] = sum[LI++];
        }
        while (RI <= R) {
            res[i++] = sum[RI++];
        }
        for (int j = 0; j < res.length; j++) {
            sum[L + j] = res[j];
        }
        return ans;
    }


}
