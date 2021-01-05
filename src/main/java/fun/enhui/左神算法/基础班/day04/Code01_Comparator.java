package fun.enhui.左神算法.基础班.day04;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 比较器
 *
 * @author 胡恩会
 * @date 2021/1/4 0:12
 */
public class Code01_Comparator {

    public static void main(String[] args) {
        Student student1 = new Student("小王", 1, 1);
        Student student2 = new Student("小李", 3, 1);
        Student student3 = new Student("小周", 2, 1);
        Student[] students = new Student[]{student1, student2, student3};
        Arrays.sort(students, new MyComparator());

        for (int i = 0; i < students.length; i++) {
            System.out.println(students[i]);
        }
    }

    /**
     * 我的学生类比较器
     **/
    public static class MyComparator implements Comparator<Student> {

        @Override
        public int compare(Student o1, Student o2) {
            // 以年龄比较
            return o1.age - o2.age;
        }
    }

    /**
     * 学生类
     **/
    public static class Student {
        String name;

        Integer age;

        Integer id;

        @Override
        public String toString() {
            return "Student{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", id=" + id +
                    '}';
        }

        public Student(String name, Integer age, Integer id) {
            this.name = name;
            this.age = age;
            this.id = id;
        }
    }
}
