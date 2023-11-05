package com.enhui.netty.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import org.junit.jupiter.api.Test;

public class ByteBufDemo {
    @Test
    public void byteBuf() {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(8, 20);
        // 非池化 堆内堆外
        UnpooledByteBufAllocator.DEFAULT.directBuffer(8, 20);
        UnpooledByteBufAllocator.DEFAULT.heapBuffer(8, 20);
        // 池化 堆内堆外
        PooledByteBufAllocator.DEFAULT.directBuffer(8, 20);
        PooledByteBufAllocator.DEFAULT.heapBuffer(8, 20);


        // 常用api
        for (int i = 0; i < 6; i++) {
            buffer.writeBytes(new byte[]{1,2,3,4});
            printBuf(buffer);
            System.out.println("-----------");
        }

    }

    private void printBuf(ByteBuf buffer) {
        System.out.println("是否可读：" + buffer.isReadable());
        System.out.println("读的索引：" + buffer.readerIndex());
        System.out.println("可读的字节数：" + buffer.readableBytes());

        System.out.println("是否可写" + buffer.isWritable());
        System.out.println("写的索引：" + buffer.writerIndex());
        System.out.println("可写的字节数：" + buffer.writableBytes());

        System.out.println("容量：" + buffer.capacity());
        System.out.println("最大的容量：" + buffer.maxCapacity());
        System.out.println("是否直接内存（堆外）：" + buffer.isDirect());
    }
}
