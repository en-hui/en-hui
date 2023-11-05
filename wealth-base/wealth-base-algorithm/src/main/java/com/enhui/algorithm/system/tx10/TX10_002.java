package com.enhui.algorithm.system.tx10;

import com.enhui.algorithm.framework.SingleParamFramework;
import com.enhui.algorithm.structure.BinaryTree;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TX10_002 extends SingleParamFramework<BinaryTree, List<Integer>> {

  private final List<Integer> solutionResult = new ArrayList<>();
  private final List<Integer> checkResult = new ArrayList<>();

  public static void main(String[] args) {
    TX10_002 instance = new TX10_002();
    instance.methodTemplate();
  }

  @Override
  protected BinaryTree randomParam1() {
    return BinaryTree.generateBinaryTree(30, 100);
  }

  @Override
  protected List<Integer> solution(BinaryTree param1) {
    if (param1 == null) {
      return solutionResult;
    }
    Stack<BinaryTree> stack = new Stack<>();
    Stack<BinaryTree> revertStack = new Stack<>();
    stack.push(param1);
    while (!stack.empty()) {
      BinaryTree pop = stack.pop();
      revertStack.push(pop);
      if (pop.getLeftNode() != null) {
        stack.push(pop.getLeftNode());
      }
      if (pop.getRightNode() != null) {
        stack.push(pop.getRightNode());
      }
    }
    while (!revertStack.empty()) {
      BinaryTree pop = revertStack.pop();
      solutionResult.add(pop.getValue());
    }
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
