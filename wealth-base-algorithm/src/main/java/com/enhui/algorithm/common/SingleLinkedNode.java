package com.enhui.algorithm.common;

import lombok.Data;

/**
 * 单链表数据结构
 */
@Data
public class SingleLinkedNode {
    private SingleLinkedNode next;
    private int value;

    public SingleLinkedNode(int value) {
        this.value = value;
    }
}
