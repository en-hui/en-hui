package fun.enhui.data.structure;

import java.util.Scanner;

/**
 * 使用数组实现队列
 * 队列只能使用一次
 * @Author HuEnhui
 * @Date 2019/9/29 23:22
 **/
public class ArrayQueueDemo {

    public static void main(String[] args) {
        ArrayQueue arrayQueue = new ArrayQueue(3);
        char key = ' ';
        Scanner scanner = new Scanner(System.in);
        boolean loop = true;
        while (loop) {
            System.out.println("s(show):显示队列");
            System.out.println("e(exit):退出程序");
            System.out.println("a(add):添加数据到队列");
            System.out.println("g(get):从队列取出数据");
            System.out.println("h(head):查看队列头部数据");
            // 接收一个字符
            key = scanner.next().charAt(0);
            switch (key){
                case 's':
                    arrayQueue.showQueue();
                    break;
                case 'e':
                    scanner.close();
                    loop = false;
                    System.out.println("程序退出");
                    break;
                case 'a':
                    System.out.println("输入一个数");
                    int value = scanner.nextInt();
                    arrayQueue.addQueue(value);
                    break;
                case 'g':
                    try {
                        int res = arrayQueue.getQueue();
                        System.out.println("取出的数据是："+res);
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case 'h':
                    try {
                        int res = arrayQueue.headQueue();
                        System.out.println("取出的数据是："+res);
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    break;

            }
        }
    }
}

/**
 * 使用数组模拟队列，编写一个ArrayQueue类
 */
class ArrayQueue {
    /**
     * 数组的最大容量
     */
    private int maxSize;
    /**
     * 队列头
     */
    private int front;
    /**
     * 队列尾
     */
    private int rear;
    /**
     * 该数组用于存放数据，模拟队列
     */
    private int[] arr;

    /**
     * 构造方法
     * @param maxSize
     * @author: HuEnhui
     * @date: 2019/9/25 13:57
     * @return:
     */
    public ArrayQueue(int maxSize) {
        this.maxSize = maxSize;
        arr = new int[maxSize];
        // 指向队列头部，front指向队列头的前一个位置
        front = -1;
        // 指向队列尾部，rear指向队列尾的数据
        rear = -1;
    }

    /**
     * 判断队列是否满了
     * @param
     * @author: HuEnhui
     * @date: 2019/9/25 13:57
     * @return: boolean
     */
    public boolean isFull() {
        return rear == maxSize - 1;
    }

    /**
     * 判断队列是否为空
     * @param
     * @author: HuEnhui
     * @date: 2019/9/25 13:58
     * @return: boolean
     */
    public boolean isEmpty() {
        return rear == front;
    }

    /**
     * 添加数据到队列
     * @param n
     * @author: HuEnhui
     * @date: 2019/9/25 14:02
     * @return: void
     */
    public void addQueue(int n) {
        // 判断队列是否满了
        if (isFull()) {
            System.out.println("队列满了");
            return;
        }
        // 尾指针后移
        rear++;
        arr[rear] = n;
    }

    /**
     * 获取队列的数据，出队列
     * @param
     * @author: HuEnhui
     * @date: 2019/9/25 14:02
     * @return: int
     */
    public int getQueue() {
        // 判断队列是否为空
        if (isEmpty()) {
            throw new RuntimeException("队列空，不能取数据");
        }
        front++;
        return arr[front];
    }

    /**
     * 展示队列的所有数据
     * @param
     * @author: HuEnhui
     * @date: 2019/9/25 14:05
     * @return: void
     */
    public void showQueue() {
        if (isEmpty()) {
            System.out.println("队列是空的，没有数据");
            return;
        }
        // 遍历打印
        for (int i = 0; i < arr.length; i++) {
            System.out.printf("arr[%d]=%d\n", i, arr[i]);
        }
    }

    /**
     * 查看头结点的值，不取出
     * @param
     * @author: HuEnhui
     * @date: 2019/9/25 14:09
     * @return: int
     */
    public int headQueue() {
        if (isEmpty()) {
            throw new RuntimeException("队列为空，没有数据");
        }
        return arr[front + 1];
    }
}
