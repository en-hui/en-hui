package com.enhui.algorithm.system.tx10;

import com.enhui.algorithm.framework.SingleParamFramework;
import com.enhui.algorithm.structure.BinaryTree;

import java.util.ArrayList;
import java.util.List;

public class TX10_002 extends SingleParamFramework<BinaryTree, List<Integer>> {

    private final List<Integer> solutionResult = new ArrayList<>();
    private final List<Integer> checkResult = new ArrayList<>();

    public static void main(String[] args) {
        TX10_002 instance = new TX10_002();
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
        last(param1);
        return checkResult;
    }

    private void last(BinaryTree head) {
        if (head == null) {
            return;
        }
        last(head.getLeftNode());
        last(head.getRightNode());
        checkResult.add(head.getValue());
    }
}
