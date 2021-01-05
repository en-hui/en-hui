package fun.enhui.左神算法.基础班.day04;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * resign 方法
 * 当堆结构已经建立完成，但这时修改了堆中的数据，堆的结构被破坏了，这时想重新组织堆结构，java中的堆不支持，就要自己完成
 *
 * @author 胡恩会
 * @date 2021/1/4 0:31
 */
public class Code03_Heap02 {

    public static void main(String[] args) {

        MyHeap myHeap = new MyHeap(new StudentComparator());
        Student s1 = new Student(6, 1, 1);
        Student s2 = new Student(3, 20, 444);
        Student s3 = new Student(7, 72, 555);
        Student s4 = new Student(1, 15, 666);
        myHeap.push(s1);
        myHeap.push(s2);
        myHeap.push(s3);
        myHeap.push(s4);
        System.out.println(myHeap.heap);
        s2.age = 6;
        myHeap.resign(s2);
        s4.age = 12;
        myHeap.resign(s4);

        System.out.println(myHeap.heap);
    }

    public static class MyHeap<T> {
        private ArrayList<T> heap;

        private HashMap<T, Integer> indexMap;

        private int heapSize;

        private Comparator<? super T> comparator;

        public MyHeap(Comparator<? super T> comparator) {
            this.comparator = comparator;
            heap = new ArrayList<>();
            indexMap = new HashMap<>();
            heapSize = 0;
        }

        public boolean isEmpty() {
            return heapSize == 0;
        }

        public int size() {
            return heapSize;
        }

        public boolean contains(T key) {
            return indexMap.containsKey(key);
        }

        public void push(T value) {
            heap.add(value);
            indexMap.put(value, heapSize);
            heapInsert(heapSize++);
        }

        private void heapInsert(int index) {
            while (comparator.compare(heap.get(index), heap.get((index - 1) / 2)) < 0) {
                swap(index, (index - 1) / 2);
                index = (index - 1) / 2;
            }
        }

        public T pop() {
            T ans = heap.get(0);
            int end = heapSize - 1;
            swap(0, end);
            heap.remove(end);
            indexMap.remove(ans);
            heapify(0, --heapSize);
            return ans;
        }

        public void heapify(int index, int heapSize) {
            int left = index * 2 + 1;
            while (left < heapSize) {
                int largest = left + 1 < heapSize && (comparator.compare(heap.get(left + 1), heap.get(left)) < 0) ? left + 1 : left;
                if (comparator.compare(heap.get(largest), heap.get(index)) < 0) {
                    swap(largest, index);
                    index = largest;
                    left = index * 2 + 1;
                }
            }
        }

        public void resign(T value) {
            int valueIndex = indexMap.get(value);
            heapInsert(valueIndex);
            heapify(valueIndex, heapSize);
        }

        public void swap(int i, int j) {
            T o1 = heap.get(i);
            T o2 = heap.get(j);
            heap.set(i, o2);
            heap.set(j, o1);
            indexMap.put(o1, j);
            indexMap.put(o2, i);
        }
    }

    public static class Student {
        public int classNo;

        public int age;

        public int id;

        public Student(int c, int a, int i) {
            classNo = c;
            age = a;
            id = i;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "classNo=" + classNo +
                    ", age=" + age +
                    ", id=" + id +
                    '}';
        }
    }

    public static class StudentComparator implements Comparator<Student> {

        @Override
        public int compare(Student o1, Student o2) {
            return o1.age - o2.age;
        }
    }
}
