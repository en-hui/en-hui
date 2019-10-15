package fun.enhui.jvm;

import java.util.Random;

/**
 * JVM参数
 * @Author: HuEnhui
 * @Date: 2019/10/15 11:07
 */
public class GetHeap {

    public static void main(String[] args){
        // 演示堆内存结构
        //返回 Java 虚拟机试图使用的最大内存量。
        long maxMemory = Runtime.getRuntime().maxMemory() ;
        //返回 Java 虚拟机中的内存总量。
        long totalMemory = Runtime.getRuntime().totalMemory() ;
        System.out.println("-Xmx:MAX_MEMORY = " + maxMemory + "（字节）、" + (maxMemory / (double)1024 / 1024) + "MB");
        System.out.println("-Xms:TOTAL_MEMORY = " + totalMemory + "（字节）、" + (totalMemory / (double)1024 / 1024) + "MB");

        // 演示堆内存溢出的过程
        String str = "huenhui";
        while(true){
            str += str + new Random().nextInt(88888888)+new Random().nextInt(999999999);
        }
    }
}
