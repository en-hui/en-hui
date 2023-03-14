package com.enhui.algorithm.structure;

import com.enhui.algorithm.util.RandomUtil;
import lombok.Data;

/**
 * 二叉树数据结构
 */
@Data
public class BinaryTree {

    private Integer value;
    private BinaryTree leftNode;
    private BinaryTree rightNode;

    public BinaryTree(Integer value) {
        this.value = value;
    }

    /**
     * 生成随机二叉树
     * ?? todo hu
     *
     * @return
     */
    public static BinaryTree generateBinaryTree(int maxHeight, int maxValue) {
        // 树的最大高度
        int height = RandomUtil.randomJust(maxHeight);
        if (height == 1) {
            return new BinaryTree(RandomUtil.random(maxValue));
        } else if (height > 1) {
            BinaryTree head = new BinaryTree(RandomUtil.random(maxValue));
            fullLRNode(head, maxValue);
            int i = 2;
            BinaryTree curr = head;
            BinaryTree left = head.getLeftNode();
            BinaryTree right = head.getRightNode();
            while (i < height) {
                i++;
                fullLRNode(left, maxValue);
                fullLRNode(right, maxValue);
            }
            return head;
        }
        return null;
    }

    public static void fullLRNode(BinaryTree head, int maxValue) {
        head.setLeftNode(new BinaryTree(RandomUtil.random(maxValue)));
        head.setRightNode(new BinaryTree(RandomUtil.random(maxValue)));
    }
}
