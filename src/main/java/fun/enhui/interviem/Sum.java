package fun.enhui.interviem;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

/**
 * @author 胡恩会
 * @date 2021/1/27 22:28
 */
public class Sum {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String src = in.next();
        // 将输入的字符串转为字符数组
        char[] chars = src.toCharArray();
        for (char c : chars) {
            if (!(c >= 'A' && c <= 'z')) {
                System.out.println("0");
                return;
            }
        }
        Stack<Character> stack = new Stack<>();
        // 遍历字符数组，放入栈
        // 放的过程中与栈顶进行比较，如果相同则弹出栈顶，如果不相同则新元素压栈
        for (int i = 0; i < chars.length; i++) {
            if (!stack.empty() && stack.peek() == chars[i]) {
                stack.pop();
            } else {
                stack.push(chars[i]);
            }

        }
        int result = 0;
        while (!stack.empty()) {
            stack.pop();
            result++;
        }
        System.out.println(result);
    }
}
