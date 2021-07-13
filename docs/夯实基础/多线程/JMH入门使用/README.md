# JMH 入门使用

## 介绍
JMH主要用于测试方法的性能   
官网：[http://openjdk.java.net/projects/code-tools/jmh/](http://openjdk.java.net/projects/code-tools/jmh/)

## 使用步骤
1.导入maven依赖   
```
<!-- JMH 测试 -->
<dependency>
    <groupId>org.openjdk.jmh</groupId>
    <artifactId>jmh-core</artifactId>
    <version>1.21</version>
</dependency>
<dependency>
    <groupId>org.openjdk.jmh</groupId>
    <artifactId>jmh-generator-annprocess</artifactId>
    <version>1.21</version>
    <scope>provided</scope>
</dependency>
```
   
2.idea安装JMH插件 【JMH plugin】（安装后需重启）   

3.由于用到了注解，需在idea中配置-打开运行程序注解配置，配置路径如下      
```File | Settings | Build, Execution, Deployment | Compiler | Annotation Processors -> 勾选Enable Annotation Processing ```

4.准备需要测试的类
```java
package fun.enhui.thread.jmh;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author 胡恩会
 * @Date 2020/8/31 22:01
 **/
public class JMHHello {
    static List<Integer> nums = new ArrayList<>();

    static {
        // 准备 1000 个随机数
        Random r = new Random();
        for (int i = 0; i < 1000; i++) {
            nums.add(1000000 + r.nextInt(1000000));
        }
    }

    /**
     * 普通 方式遍历
     *
     * @Author: 胡恩会
     * @Date: 2020/8/31 22:13
     * @return: void
     **/
    static void foreach() {
        nums.forEach(JMHHello::isPrime);
    }

    /**
     * 并行处理流 方式遍历
     *
     * @Author: 胡恩会
     * @Date: 2020/8/31 22:14
     * @return: void
     **/
    static void parallel() {
        nums.parallelStream().forEach(JMHHello::isPrime);
    }

    /**
     * 判断一个数是否是质数
     *
     * @param num:
     * @Author: 胡恩会
     * @Date: 2020/8/31 22:14
     * @return: boolean
     **/
    static boolean isPrime(int num) {
        for (int i = 2; i <= num / 2; i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }
}

```

5.写单元测试(必须在test package下面)
```java
 package fun.enhui.thread.jmh;
 
 import org.openjdk.jmh.annotations.*;
 
 /**
  * JMH 使用
  *
  * @Author 胡恩会
  * @Date 2020/8/31 21:49
  **/
 public class JMHHelloTest {
 
     /**
      * 普通遍历的性能
      *
      * @Author: 胡恩会
      * @Date: 2020/8/31 22:31
      * @return: void
      **/
     @Benchmark
     @Warmup(iterations = 1, time = 3)
     @Fork(5)
     @BenchmarkMode(Mode.Throughput)
     @Measurement(iterations = 1, time = 3)
     public void testForEach() {
         JMHHello.foreach();
     }
 
     /**
      * 并行流遍历的性能
      *
      * @Author: 胡恩会
      * @Date: 2020/8/31 22:33
      * @return: void
      **/
     @Benchmark
     @Warmup(iterations = 1, time = 3)
     @Fork(5)
     @BenchmarkMode(Mode.Throughput)
     @Measurement(iterations = 1, time = 3)
     public void testParallel() {
         JMHHello.parallel();
     }
 }

 ```
 
 6.假如运行报错   
 错误信息：``` ERROR: org.openjdk.jmh.runner.RunnerException: ERROR: Exception while trying to acquire the JMH lock (C:\WINDOWS\/jmh.lock): 拒绝访问。, exiting. Use -Djmh.ignoreLock=true to forcefully continue.
          	at org.openjdk.jmh.runner.Runner.run(Runner.java:216)
          	at org.openjdk.jmh.Main.main(Main.java:71) ```   
 解决方案(每个方法都要加)：打开运行参数【RunConfiguration】 -> 在【Environment Variables】一栏后面点击选择 -> 勾选include system environment viables         	
 
 7.得到测试报告结果
 
 ## JMH 基本概念
1. Warmup 预热，由于JVM中对于特定代码会存在优化（本地化），预热对于测试结果很重要

2. Mesurement 总共执行多少次测试

3. Timeout

4. Threads 线程数，由fork指定

5. Benchmark mode 基准测试的模式

6. Benchmark 测试哪一段代码

## 其他参考案例
[官方样例](http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/)