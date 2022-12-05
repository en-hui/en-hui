# 算法-体系学习

## day01
2022-11-28
> 评估算法的核心指标
>
> 对数器
>
> 排序    
> 1.选择排序（Sorted_Select）   
> 2.冒泡排序（Sorted_Bubble）   
> 3.插入排序（Sorted_Insert）
>
> 二分法   
> 1.有序数组中，查找某个数字是否存在（BinarySelect_NumExist）   
> 2.有序数组中，查找>=某个数字最左侧的位置（BinarySelect_NearLeft）   
> 3.有序数组中，查找<=某个数字最右侧的位置（BinarySelect_NearRight）   
> 4.局部最小问题（BinarySelect_Awesome）

## day02
2022-11-29
> 异或   
> 1.不借助第三个变量，交换两个变量的值（Eor_Swap）     
> 2.一个数组中有一个数字出现了奇数次，其他数都出现了偶数次，找到并打印这个数（Eor_OneNumOddNumberTimes）    
> 3.一个数组中有两个数字出现了奇数次a和b，其他数都出现了偶数次，找到并打印这两个数（Eor_TowNumOddNumberTimes）   
> 4.一个数组中有一种数出现K次，其他数都出现M次，M>1,K<M.找到出现了K次的数，要求时间复杂度O(n),额外空间复杂度O(1)（Eor_OneKOtherM）   

## day03
2022-11-30、2022-12-01、2022-12-04
> 单向链表和双向链表    
> 1.单链表的反转（SingleLinked_Reverse）    
> 2.双链表的反转（DoubleLinked_Reverse）    
> 3.单链表把给定值都删除（SingleLinked_DeleteNum）     
> 4.双链表把给定值都删除（DoubleLinked_DeleteNum）
>
> 栈和队列    
> 栈：push-进栈 pop-出栈     
> 队列：offer-进队列 poll-出队列   
> 1.双向链表实现栈（StackFromDoubleLinked）    
> 2.双向链表实现队列（QueueFromDoubleLinked）   
> 3.数组实现栈（StackFromArray）    
> 4.数组实现队列（QueueFromArray）   
> 5.实现一个特殊栈，除了常规都pop和push，在提供一个getMin，
> 可以使用现成的栈结构，要求三个方法的时间复杂度都是O(1)（MinStackFromStack）     
> 6.如何使用栈结构实现队列结构（QueueFromStack）    
> 7.如何使用队列结构实现栈结构（StackFromQueue）    
> 
> 递归    
> 1.一个数组找最大值，用递归实现(理解递归)    
> 2.Master公式:用来计算递归函数的时间复杂度(只有子函数的规模一致才能使用)    
>
> 哈希表和有序表

## day04
2022-12-04
> 归并排序 O(N * logN)   
> 1.递归实现归并（Sorted_Merge）    
> 2.非递归实现归并(步长)（Sorted_Merge）   
> 3.小和问题，一个数组，计算每个位置左边比他小的数据的和，要求O(N * logN)（SortedMerge_Sum）   
> 4.数组中有多少个逆序对，逆序对：任意两个数据（不用相邻），右边比左边小（SortedMerge_ReversePair）      
> 5.数组中某一位置的num右边有多少个数，乘以2依然比num小，求整个数组中每个数的这种个数的总和（StortedMerge_Twice）

## day05
2022-12-04
> 归并排序（TODO CODE：太难了，没理解，有时间多看几遍）   
> 6.给定一个数组arr，两个整数lower和upper，返回arr中有多少个子数组的累加和在 [lower,upper] 范围上   
> leetcode题目：https://leetcode.cn/problems/count-of-range-sum/       
> 解法1：归并+前缀和（SortedMerge_CountRangeSum）      
> 解法2：有序表，TODO   
> 
> 快速排序（Sorted_Quick）   
> 1.荷兰国旗问题   
> 1.1、给定一个数组和一个整数，将数组分为小于等于区域和大于区域   
> 1.2、给定一个数组和一个整数，将数组分为小于区域，等于区域和大于区域    
> 快排1.0：荷兰国旗1.1+递归     
> 快排2.0：荷兰国旗1.2+递归     
> 快排3.0（随机快排）：快排v2加工，不取最右侧的数，而是随机一个位置的数    

## day06
2022-12-05
> 比较器 实现Comparator接口，重写compare方法      
> 返回负数表示第一个参数应该排在前面   
> 返回整数表示第二个参数应该排在前面   
> 返回0，说明两个数相等，无所谓   
> 
> 堆---也叫 优先级队列（Java中的PriorityQueue-默认小根堆）   
> 完全二叉树、大根堆、小根堆    
> 1.用数组实现大根堆，支持 offer(heapInsert) 和 poll(heapify)（Heap）    
> 2.堆排序（Sorted_Heap）    
> 3.已知一个几乎有序的数组，几乎有序是指，如果把数组排好顺序的话，每个元素移动的距离一定不超过k，并且k相对数组长度是比较小的。选择一个合适的排序策略对数组排序（Heap_SortedK）   

## day07
2022-12-06
> 
> 

## day08
