package fun.enhui.左神算法.基础班.day02;

import java.util.Arrays;

/**
 * 数组实现栈和队列
 *
 * @author 胡恩会
 * @date 2021/1/1 17:45
 */
public class Code04_RingArray {

    /**
     * 固定长度的栈，假设长度仅维持7
     **/
    public static class MyStack {
        /**
         * 数组容量
         **/
        private int capacity = 7;

        /**
         * push index
         **/
        private int index = 0;

        private int arr[];

        public MyStack() {
            arr = new int[capacity];
        }

        public void push(int value) {
            if (index == capacity) {
                throw new RuntimeException("栈满了,不能在加了");
            }
            arr[index] = value;
            index++;
        }

        public int pop() {
            if (index == 0) {
                throw new RuntimeException("栈空了，不能在取了");
            }
            index--;
            return arr[index];
        }
    }

    /**
     * 固定长度的队列，假设长度仅维持7
     * 分别记录下一次存的位置和下一次取的位置
     * 记录当前元素数量size
     **/
    public static class MyQueue {
        int[] arr;

        /**
         * 数组容量
         **/
        int capacity = 7;

        /**
         * 下一次存的位置
         **/
        int pushIndex = 0;

        /**
         * 下一次取的位置
         **/
        int popIndex = 0;

        int size = 0;

        public MyQueue() {
            arr = new int[capacity];
        }

        public void push(int value) {
            if (size == capacity) {
                throw new RuntimeException("满了，不能在放了");
            }
            size++;
            arr[pushIndex] = value;
            pushIndex = nextIndex(pushIndex);
        }

        private int nextIndex(int index) {
            // 环形
            return index < capacity - 1 ? index + 1 : 0;
        }

        public int pop() {
            if (size == 0) {
                throw new RuntimeException("空了，不能在取了");
            }
            size--;
            int result = arr[popIndex];
            popIndex = nextIndex(popIndex);
            return result;
        }
    }


    public static void main(String[] args) {
        System.out.println("----------------栈---------------");
        MyStack stack = new MyStack();
        for (int i = 0; i < stack.capacity; i++) {
            stack.push(i);
        }
        System.out.println("栈中的所有元素：" + Arrays.toString(stack.arr));
        for (int i = 0; i < stack.capacity; i++) {
            System.out.println("取出栈中元素" + stack.pop() + " ");
        }

        System.out.println("-----------------队列----------------------");
        MyQueue queue = new MyQueue();
        for (int i = 0; i < queue.capacity; i++) {
            queue.push(i);
        }
        System.out.println("队列中的所有元素：" + Arrays.toString(queue.arr));
        for (int i = 0; i < queue.capacity; i++) {
            int pop = queue.pop();
            System.out.println("取出队列中元素：" + pop);
        }
    }
}
