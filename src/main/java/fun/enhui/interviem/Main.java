package fun.enhui.interviem;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * @author 胡恩会
 * @date 2021/1/27 23:08
 */
public class Main {
    public static void main(String[] args) {
        // 数据准备
        Scanner in = new Scanner(System.in);
        int count = in.nextInt();
        int[] data = new int[count];
        for (int i = 0; i < count; i++) {
            data[i] = in.nextInt();
        }

        // 排序
        Arrays.sort(data);

        // 从小到达计算
        int n = 0;
        int base = data[0];
        LinkedList<Integer> help = new LinkedList();
        for (int i = 1; i < count; i++) {
            // 如果不能整除，放入help中
            if (data[i] % base != 0) {
                help.add(data[i]);
            }
        }
        n++;
        // 链表第一个最小的数做base
        LinkedList<Integer> data1 = help;
        while (!data1.isEmpty()) {
            help = new LinkedList<>();
            base = data1.getFirst();
            for (Integer integer : data1) {
                if (integer % base != 0) {
                    help.add(integer);
                }
            }
            data1 = help;
            n++;
        }
        System.out.println(n);
    }
}
