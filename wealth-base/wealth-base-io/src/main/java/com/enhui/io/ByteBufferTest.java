package com.enhui.io;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class ByteBufferTest {

    /**
     * 演示ByteBuffer <br>
     * [pos=0 lim=8192 cap=8192]<br>
     * pos（position）：位置，初始值为0<br>
     * lim（limit）：限制，初始值为容量大小<br>
     * cap（capacity）：容量，初始值为容量大小<br>
     */
    @Test
    public void whatByteBuffer() {
        // 在jvm的堆上申请一块内存空间
        ByteBuffer byteBuffer = ByteBuffer.allocate(8192);
        // 在堆外申请一块内存空间
        ByteBuffer directByteBuffer = ByteBuffer.allocateDirect(8192);
        ByteBuffer buffer = byteBuffer;
        // [pos=0 lim=8192 cap=8192]
        System.out.println("初始：" + buffer);

        // [pos=3 lim=8192 cap=8192]
        // 写入了三个字节，所以位置到了3
        buffer.put("123".getBytes());
        System.out.println("put 后：" + buffer);

        // [pos=0 lim=3 cap=8192]
        // 每次要读，就使用flip进行翻转，把pos设为0，limit设为已写位置
        buffer.flip();
        System.out.println("flip 后：" + buffer);

        // [pos=1 lim=3 cap=8192]
        // 获取一个字节，取出，所以pos位置走到1
        buffer.get();
        System.out.println("get 后：" + buffer);

        // [pos=2 lim=8192 cap=8192]
        // 每次要写，就使用compact，把已经读的位置挤压掉，所有内容前移,所以0被取出了，所有前移，pos为2，该写3位置了
        buffer.compact();
        System.out.println("compact 后：" + buffer);

        // [pos=0 lim=8192 cap=8192]
        // 清空内容，所有位置重置回最初样子
        buffer.clear();
        System.out.println("clear 后：" + buffer);
    }

}
