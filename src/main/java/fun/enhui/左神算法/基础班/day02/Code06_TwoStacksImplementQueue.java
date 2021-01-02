package fun.enhui.左神算法.基础班.day02;

import java.util.Stack;

/**
 * 用两个栈实现队列效果
 * 一个push栈，一个pop栈
 * 每次存往push栈存，想取的时候，先把push栈的元素倒腾到pop栈
 * 两个原则：
 * 只有pop栈空了才可以倒
 * 从push栈倒出数据，必须一次性倒腾空
 *
 * @author 胡恩会
 * @date 2021/1/2 11:28
 */
public class Code06_TwoStacksImplementQueue {
    private Stack<Integer> pushStack;
    private Stack<Integer> popStack;

    public Code06_TwoStacksImplementQueue() {
        this.pushStack = new Stack<>();
        this.popStack = new Stack<>();
    }

    /**
    * 把push栈的东西倒腾到pop栈
     **/
    private void pushToPop() {
        // 1.只有pop栈是空的才可以倒
        if (this.popStack.isEmpty()) {
            // 2.必须把push栈倒空才能结束
            while (!this.pushStack.isEmpty()) {
                this.popStack.push(this.pushStack.pop());
            }
        }

    }

    public void add(int pushInt){
        pushStack.push(pushInt);
        this.pushToPop();
    }

    public int pop() {
        if (pushStack.empty() && popStack.empty()) {
            throw new RuntimeException("队列已经空了");
        }
        pushToPop();
        return popStack.pop();
    }

    public int peek() {
        if (pushStack.empty() && popStack.empty()) {
            throw new RuntimeException("队列已经空了");
        }
        pushToPop();
        return popStack.peek();
    }

    public static void main(String[] args) {
        int count = 3;
        Code06_TwoStacksImplementQueue queue = new Code06_TwoStacksImplementQueue();
        for (int i = 0; i < count; i++) {
            queue.add(i);
        }

        for (int i = 0; i < count; i++) {
            System.out.println(queue.pop());
        }
    }
}
