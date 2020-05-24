package fun.enhui.data.structure.leetcode;

/**
 * 给定两个大小为 m 和 n 的正序（从小到大）数组 nums1 和 nums2。
 * 请你找出这两个正序数组的中位数，并且要求算法的时间复杂度为 O(log(m + n))。
 * 你可以假设 nums1 和 nums2 不会同时为空。
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/median-of-two-sorted-arrays
 *
 * @Author 胡恩会
 * @Date 2020/5/24 20:07
 **/
public class Day01_FindMedianSortedArrays {
    public static void main(String[] args) {
        int[] nums1 = {1,2};
        int[] nums2 = {3,4};
        double result = findMedianSortedArrays(nums1,nums2);
        System.out.println(result);
    }

    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        // nums1是空，取nums2的中位数
        if(nums1 == null || nums1.length == 0) {
            return getMedian(nums2);
        }
        // nums2是空，取nums1的中位数
        if(nums2 == null || nums2.length == 0) {
            return getMedian(nums1);
        }
        // 合并nums1和nums2 ，取中位数
        int[] nums3 = twoArraySort(nums1,nums2);
        return getMedian(nums3,nums1.length + nums2.length);
    }
    private static double getMedian(int[] arr){
        int len = arr.length;
        return getMedian(arr,len);
    }
    private static double getMedian(int[] arr,int len){
        return len % 2 ==0 ? (arr[len/2-1]+arr[len/2])/2.0 : arr[len/2];
    }

    public static int[] twoArraySort(int[] nums1,int[] nums2){
        int len = nums1.length + nums2.length;// 获取总长度
        boolean end1 = false,end2=false;//数组是否结束
        int[] nums3 = new int[len];//合并后的数组
        int nums1Posi=0,nums2Posi=0;
        for (int i = 0; i <= len/2; i++) {
            if (end1){
                nums3[i]=nums2[nums2Posi];
                nums2Posi++;
            }else if (end2){
                nums3[i]=nums1[nums1Posi];
                nums1Posi++;
            }else if (!end2 && nums1[nums1Posi] > nums2[nums2Posi]) {
                nums3[i] = nums2[nums2Posi];
                nums2Posi++;
                if (nums2Posi == nums2.length){
                    end2 = true;
                }
            } else if (!end1 && nums1[nums1Posi] <= nums2[nums2Posi]) {
                nums3[i] = nums1[nums1Posi];
                nums1Posi++;
                if (nums1Posi == nums1.length){
                    end1 = true;
                }
            }
        }
        return nums3;
    }
}
