package fun.enhui.左神算法.基础班.day02;

import java.util.Stack;

/**
 * @author 胡恩会
 * @date 2021/1/1 21:33
 */
public class Code05_GetMinStack {

    public static class MyStack1 {
        private Stack<Integer> stackData;

        private Stack<Integer> stackMin;

        public MyStack1() {
            stackData = new Stack<>();
            stackMin = new Stack<>();
        }

        public void push(int newNum) {
            if (stackMin.isEmpty()) {
                stackMin.push(newNum);
            } else if (newNum <= this.getMin()) {
                stackMin.push(newNum);
            }
            this.stackData.push(newNum);
        }

        public int pop() {
            if (this.stackData.isEmpty()) {
                throw new RuntimeException("你的栈已经空了");
            }
            int value = this.stackData.pop();
            if (value == this.getMin()) {
                this.stackMin.pop();
            }
            return value;
        }

        public int getMin() {
            if (stackMin.isEmpty()) {
                throw new RuntimeException("你的栈已经空了");
            }
            return stackMin.peek();
        }
    }

    /**
     * 用两个栈存储数据 stackData 和 stackMin
     * 每次加元素，stackData正常加，stactMin加（栈顶和新加 两者取最小）
     * 每次取元素，两者同步取
     * 取最小值，直接取最小栈顶
     **/
    public static class MyStack2 {
        private Stack<Integer> stackData;

        private Stack<Integer> stackMin;

        public MyStack2() {
            stackData = new Stack<>();
            stackMin = new Stack<>();
        }

        public void push(int value) {
            if (stackData.isEmpty()) {
                stackData.push(value);
                stackMin.push(value);
            } else {
                stackData.push(value);
                // 范围最小 = 新加的值 和 最小栈顶 比较取最小
                int rangMin = value < stackMin.peek() ? value : stackMin.peek();
                stackMin.push(rangMin);
            }
        }

        public int pop() {
            if (stackData.isEmpty()) {
                throw new RuntimeException("栈空了，不能在取了");
            }
            int result = stackData.pop();
            stackMin.pop();
            return result;
        }

        public int getMin() {
            return stackMin.peek();
        }
    }

    public static void main(String[] args) {
        int capacity = 10;
        MyStack2 stack2 = new MyStack2();
        for (int i = 0; i < capacity; i++) {
            stack2.push((int) (10 * Math.random()) - (int) (10 * Math.random()));
        }
        int min = stack2.getMin();
        System.out.println("栈中最小值为：" + min);
        System.out.print("栈中元素分别为：");
        for (int i = 0; i < capacity; i++) {
            System.out.print(stack2.pop() + " ");
        }
    }

}
