# 算法-体系学习

## day01 认识复杂度、对数器、二分法

[TX01](夯实基础/数据结构与算法/体系学习/TX01.md)  
> 评估算法的核心指标
>
> 对数器
>
> 排序    
> 【TX01_001】：1.选择排序（Sorted_Select）   
> 【TX01_002】：2.冒泡排序（Sorted_Bubble）   
> 【TX01_003】：3.插入排序（Sorted_Insert）
>
> 二分法   
> 【TX01_004】：1.有序数组中，查找某个数字是否存在（BinarySelect_NumExist）   
> 【TX01_005】：2.有序数组中，查找>=某个数字最左侧的位置（BinarySelect_NearLeft）   
> 【TX01_006】：3.有序数组中，查找<=某个数字最右侧的位置（BinarySelect_NearRight）   
> 【TX01_007】：4.局部最小问题（BinarySelect_Awesome）

## day02 异或运算相关面试题
[TX02](夯实基础/数据结构与算法/体系学习/TX02.md)    
> 异或   
> 【TX02_001】：1.不借助第三个变量，交换两个变量的值（Eor_Swap）     
> 【TX02_002】：2.一个数组中有一个数字出现了奇数次，其他数都出现了偶数次，找到并打印这个数（Eor_OneNumOddNumberTimes）    
> 【TX02_003】：3.提取一个整数，二进制中最右侧的1     
> 【TX02_004】：4.一个数组中有两个数字出现了奇数次a和b，其他数都出现了偶数次，找到并打印这两个数（Eor_TowNumOddNumberTimes）   
> 【TX02_005】：5.一个数组中有一种数出现K次，其他数都出现M次，M>1,K<M.找到出现了K次的数，要求时间复杂度O(n),额外空间复杂度O(1)（Eor_OneKOtherM）

## day03 一些基础的数据结构
[TX03](夯实基础/数据结构与算法/体系学习/TX03.md)
> 单向链表和双向链表    
> 【TX03_001】：1.单链表的反转（SingleLinked_Reverse）    
> 【TX03_002】：2.双链表的反转（DoubleLinked_Reverse）    
> 【TX03_003】：3.单链表把给定值都删除（SingleLinked_DeleteNum）     
> 【TX03_004】：4.双链表把给定值都删除（DoubleLinked_DeleteNum）
>
> 栈和队列    
> 栈：push-进栈 pop-出栈     
> 队列：offer-进队列 poll-出队列   
> 【TX03_005】：1.双向链表实现栈（StackFromDoubleLinked）    
> 【TX03_006】：2.双向链表实现队列（QueueFromDoubleLinked）   
> 【TX03_007】：3.数组实现栈（StackFromArray）    
> 【TX03_008】：4.数组实现队列（QueueFromArray）   
> 【TX03_009】：5.实现一个特殊栈，除了常规都pop和push，在提供一个getMin，
> 可以使用现成的栈结构，要求三个方法的时间复杂度都是O(1)（MinStackFromStack）     
> 【TX03_010】：6.如何使用栈结构实现队列结构（QueueFromStack）    
> 【TX03_011】：7.如何使用队列结构实现栈结构（StackFromQueue）
>
> 递归    
> 【TX03_012】：1.一个数组找最大值，用递归实现(理解递归)    
> 2.Master公式:用来计算递归函数的时间复杂度(只有子函数的规模一致才能使用)
>
> 哈希表和有序表

## day04 归并排序及其相关面试题
[TX04](夯实基础/数据结构与算法/体系学习/TX04.md)
> 归并排序 O(N * logN)   
> 【TX04_001】：1.递归实现归并（Sorted_Merge）    
> 【TX04_002】：2.非递归实现归并(步长)（Sorted_Merge）   
> 【TX04_003】：3.小和问题，一个数组，计算每个位置左边比他小的数据的和，要求O(N * logN)（SortedMerge_Sum）   
> 【TX04_004】：4.数组中有多少个逆序对，逆序对：任意两个数据（不用相邻），右边比左边小（SortedMerge_ReversePair）      
> 【TX04_005】：5.数组中某一位置的num右边有多少个数，乘以2依然比num小，求整个数组中每个数的这种个数的总和（StortedMerge_Twice）

## day05 归并排序附加题、随机快速排序
[TX05](夯实基础/数据结构与算法/体系学习/TX05.md)
> 【TX05_001】：归并排序（TODO CODE：太难了，没理解，有时间多看几遍）   
> 6.给定一个数组arr，两个整数lower和upper，返回arr中有多少个子数组的累加和在 [lower,upper] 范围上   
> leetcode题目：https://leetcode.cn/problems/count-of-range-sum/       
> 解法1：归并+前缀和（SortedMerge_CountRangeSum）      
> 解法2：有序表，TODO
>
> 快速排序（Sorted_Quick）   
> 1.荷兰国旗问题   
> 【TX05_002】：1.1、给定一个数组和一个整数，将数组分为小于等于区域和大于区域   
> 【TX05_003】：1.2、给定一个数组和一个整数，将数组分为小于区域，等于区域和大于区域    
> 【TX05_004】：快排   
> 快排1.0：荷兰国旗1.1+递归     
> 快排2.0：荷兰国旗1.2+递归     
> 快排3.0（随机快排）：快排v2加工，不取最右侧的数，而是随机一个位置的数

## day06 堆和堆排序
[TX06](夯实基础/数据结构与算法/体系学习/TX06.md)
> 比较器 实现Comparator接口，重写compare方法      
> 返回负数表示第一个参数应该排在前面   
> 返回整数表示第二个参数应该排在前面   
> 返回0，说明两个数相等，无所谓
>
> 堆---也叫 优先级队列（Java中的PriorityQueue-默认小根堆）   
> 完全二叉树、大根堆、小根堆    
> 【TX06_001】：1.用数组实现大根堆，支持 offer(heapInsert) 和 poll(heapify)（Heap）    
> 【TX06_002】：2.堆排序（Sorted_Heap）    
> 【TX06_003】：3.已知一个几乎有序的数组，几乎有序是指，如果把数组排好顺序的话，每个元素移动的距离一定不超过k，并且k相对数组长度是比较小的。选择一个合适的排序策略对数组排序（Heap_SortedK）

## day07 加强堆
[TX07](夯实基础/数据结构与算法/体系学习/TX07.md)
> 堆   
> 【TX07_001】：1.最大线段重合问题   
> 【TX07_002】：2.加强堆（实现一个堆结构，支持isEmpty、size、contains、peek、pop、push、remove操作）   
> 【TX07_003】：3.加强堆（模拟电商topk得奖）

## day08 前缀树、不基于比较的排序、排序稳定性
[TX08](夯实基础/数据结构与算法/体系学习/TX08.md)
> 【TX08_001】：前缀树（TrieTree）
>
> 桶排序（思想）
> 计数排序     
> 基数排序
>
> 排序总结