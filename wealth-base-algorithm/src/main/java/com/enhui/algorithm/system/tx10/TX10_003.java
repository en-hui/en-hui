package com.enhui.algorithm.system.tx10;

import com.enhui.algorithm.framework.SingleParamFramework;
import com.enhui.algorithm.structure.BinaryTree;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
    if (param1 == null) {
      return solutionResult;
    }
    Stack<BinaryTree> stack = new Stack<>();
    BinaryTree cur = param1;
    stack.push(cur);
    while (!stack.empty()) {
      while (cur.getLeftNode() != null) {
        cur = cur.getLeftNode();
        stack.push(cur);
      }
      while (!stack.empty()) {
        cur = stack.pop();
        solutionResult.add(cur.getValue());
        if (cur.getRightNode() != null) {
          cur = cur.getRightNode();
          stack.push(cur);
          break;
        }
      }
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
