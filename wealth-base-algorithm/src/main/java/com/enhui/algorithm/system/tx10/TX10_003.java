package com.enhui.algorithm.system.tx10;

import com.enhui.algorithm.framework.SingleParamFramework;
import com.enhui.algorithm.structure.BinaryTree;

import java.util.ArrayList;
import java.util.List;

public class TX10_003 extends SingleParamFramework<BinaryTree, List<Integer>> {

    private final List<Integer> solutionResult = new ArrayList<>();
    private final List<Integer> checkResult = new ArrayList<>();

    public static void main(String[] args) {
        TX10_003 instance = new TX10_003();
        instance.methodTemplate();
    }

    @Override
    protected BinaryTree randomParam1() {
        return BinaryTree.generateBinaryTree(3, 5);
    }

    @Override
    protected List<Integer> solution(BinaryTree param1) {
        return solutionResult;
    }

    /**
     * 递归方案
     *
     * @param param1 二叉树头节点
     * @return
     */
    @Override
    protected List<Integer> check(BinaryTree param1) {
        middle(param1);
        return checkResult;
    }

    private void middle(BinaryTree head) {
        if (head == null) {
            return;
        }
        middle(head.getLeftNode());
        checkResult.add(head.getValue());
        middle(head.getRightNode());
    }
}
