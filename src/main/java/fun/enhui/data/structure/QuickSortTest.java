package fun.enhui.data.structure;

/**
 * 快速排序
 * @Author HuEnhui
 * @Date 2019/9/29 23:19
 **/
public class QuickSortTest {
    public static void main(String[] args) {
        int[] nums = {1,9,6,4,2,5,7};
        quickSort(nums,0,nums.length-1);
        for (int i = 0; i < nums.length; i++) {
            System.out.print(nums[i]+",");
        }
    }

    /**
     *  快速排序，对数组进行排序
     * @author: HuEnhui
     * @date: 2019/9/29 17:32
     * @param nums 要排序的数组
     * @param start 开始下标
     * @param end 结束下标
     * @return: int[]
     */
    static void quickSort(int[] nums,int start,int end){
        if(nums.length==0 || nums==null){
            return;
        }
        if(start > end){
            return;
        }else{
            // 最初排序，得到第一个基准数字的位置
            int position = single(nums,start,end);
            // 分开两份后  左右分别排序
            quickSort(nums,start,position-1);
            quickSort(nums,position+1,end);
        }


    }

    /**
     *  单次循环所做的排序
     * @author: HuEnhui
     * @date: 2019/9/29 17:32
     * @param nums
     * @param start
     * @param end
     * @return: int
     */
    static int single(int[] nums,int start,int end) {
        // 最后一个作为基准数字
        int bashNum = nums[end];


        while (start < end) {
            // 左边数字小于基准数字 则下标右移
            while (start < end && nums[start] <= bashNum) {
                start++;
            }
            // 左边数字大于基准数字 交换
            if (start < end) {
                int temp = nums[end];
                nums[end] = nums[start];
                nums[start] = temp;
                end--;
            }
            // 右边数字大于基准数字
            while (start < end && nums[end] > bashNum) {
                end--;
            }
            // 右边数字小于基准数字 交换
            if (start < end) {
                int temp = nums[end];
                nums[end] = nums[start];
                nums[start] = temp;
                start++;
            }
        }
        return end;
    }
}
