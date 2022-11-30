package com.enhui.algorithm.common;

import lombok.Data;

/**
 * 双链表数据结构
 */
@Data
public class DoubleLinkedNode {
    private SingleLinkedNode last;
    private SingleLinkedNode next;
    private int value;

    public DoubleLinkedNode(int value) {
        this.value = value;
    }
}
