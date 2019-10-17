package fun.enhui.interview;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 使用AtomicReference包装User类
 * @Author: HuEnhui
 * @Date: 2019/10/17 11:21
 */
public class AtomicReferenceDemo {
    public static void main(String[] args) {
        User zhangsan = new User("zhangsan",22);
        User lisi = new User("lisi",25);

        AtomicReference<User> atomicReference = new AtomicReference<>();
        atomicReference.set(zhangsan);

        System.out.println(atomicReference.compareAndSet(zhangsan,lisi)+"\t"+atomicReference.get().toString());
        System.out.println(atomicReference.compareAndSet(zhangsan,lisi)+"\t"+atomicReference.get().toString());
    }
}

class User{
    String userName;
    int age;

    public String getUserName() {
        return userName;
    }

    public int getAge() {
        return age;
    }

    public User(String userName, int age) {
        this.userName = userName;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", age=" + age +
                '}';
    }
}
