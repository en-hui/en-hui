# 数据结构与算法-基础

### 评估算法的核心指标
1. 时间复杂度
2. 额外空间复杂度
3. 常数项时间操作

> 对数器：一个随机样本生成器，一个肯定正确的算法，一个想要测试的算法

排序的对数器：[排序的对数器代码](https://github.com/Hu-enhui/study-code/blob/master/src/main/java/fun/enhui/data/structure/base/SortCheck.java)   
选择排序(时间复杂度O(n<sup>2</sup>)):[选择排序代码](https://github.com/Hu-enhui/study-code/blob/master/src/main/java/fun/enhui/data/structure/base/Day01_SelectionSort.java)    
冒泡排序(时间复杂度O(n<sup>2</sup>)):[冒泡排序代码](https://github.com/Hu-enhui/study-code/blob/master/src/main/java/fun/enhui/data/structure/base/Day01_BubbleSort.java)   
插入排序(时间复杂度O(n<sup>2</sup>)):[插入排序代码](https://github.com/Hu-enhui/study-code/blob/master/src/main/java/fun/enhui/data/structure/base/Day01_InsertSort.java)   

### 二分法
1.在一个有序数组中，找出某个数的位置：[有序数组二分查找某数字是否存在](https://github.com/Hu-enhui/study-code/blob/master/src/main/java/fun/enhui/data/structure/base/Day01_BinarySearch01.java)     
2.在一个有序数组中，找出大于等于某个数的最左侧位置：[有序数组二分查找>=某数字最左侧位置](https://github.com/Hu-enhui/study-code/blob/master/src/main/java/fun/enhui/data/structure/base/Day01_BinarySearch02.java)   
3.在一个有序数组中，找出小于等于某个数的最右侧位置：[有序数组二分查找<=某数字最右侧位置](https://github.com/Hu-enhui/study-code/blob/master/src/main/java/fun/enhui/data/structure/base/Day01_BinarySearch03.java)   
4.在一个无序数组中，找出局部最小值的下标位置：[无序数组二分查找局部最小](https://github.com/Hu-enhui/study-code/blob/master/src/main/java/fun/enhui/data/structure/base/Day01_BinarySearch04.java)

### 异或运算
简单记，异或运算就是无进位相加。记住两个计算：``` 0^N=N  N^N=0 ```   
1.不用中间变量，交换两个值（了解即可，两个值的内存地址相同不可以用）：   
```
int a=1;int b=2; 
a=a^b;
b=a^b;
a=a^b; 
``` 
2.一个数组中有一个数出现了奇数次，其他数出现了偶数次，找出这个奇数次的数：[异或找到出现奇数次的一个数](https://github.com/Hu-enhui/study-code/blob/master/src/main/java/fun/enhui/data/structure/base/Day01_Xor01.java)   
3.一个int类型的数，提取出二进制数最右侧的1:```num & ((~num) + 1)```       
4.一个数组中有两个数出现了奇数次，其他数出现了偶数次，找出这两个奇数次的数：[异或找到出现奇数次的两个数](https://github.com/Hu-enhui/study-code/blob/master/src/main/java/fun/enhui/data/structure/base/Day01_Xor03.java)   