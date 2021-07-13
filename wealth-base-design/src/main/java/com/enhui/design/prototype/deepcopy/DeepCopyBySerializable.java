package com.enhui.design.prototype.deepcopy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.*;

/**
 * 通过对象序列化实现深拷贝
 * @author: HuEnhui
 * @date: 2019/12/23 10:00
 */
public class DeepCopyBySerializable {
    public static void main(String[] args) {
        SheepTarget1 sheepTarget = new SheepTarget1("内对象",2);
        Sheep1 sheep = new Sheep1("外对象",sheepTarget);
        Sheep1 sheep1 = (Sheep1) sheep.deepClone();

        System.out.println(sheep);
        System.out.println(sheep1);
    }
}

/**
 * 重写clone实现深拷贝
 * （外部对象，包含一个引用类型成员变量）
 * @author: HuEnhui
 * @date: 2019/12/23 9:25
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
class Sheep1 implements Serializable {
    private String name;
    private SheepTarget1 sheepTarget;

    public Object deepClone() {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;

        try {
            // 序列化
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            // 当前对象以对象流的方式输出
            oos.writeObject(this);

            // 反序列化
            bis = new ByteArrayInputStream(bos.toByteArray());
            ois = new ObjectInputStream(bis);
            Sheep1 sheep1 = (Sheep1) ois.readObject();

            return sheep1;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            // 关闭流
            try{
                bos.close();
                oos.close();
                bis.close();
                ois.close();
            }catch (Exception e1){
                System.out.println(e1.getMessage());
            }
        }
    }
}

/**
 * 使用默认clone实现浅拷贝
 * （内部对象）
 * @author: HuEnhui
 * @date: 2019/12/23 9:56
 */
@Setter
@Getter
@AllArgsConstructor
class SheepTarget1 implements Serializable{
    private String name;
    private int age;
}