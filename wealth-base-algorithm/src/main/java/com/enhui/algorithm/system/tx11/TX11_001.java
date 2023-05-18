package com.enhui.algorithm.system.tx11;

import com.enhui.algorithm.framework.SingleParamFramework;
import com.enhui.algorithm.structure.BinaryTree;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TX11_001 extends SingleParamFramework<BinaryTree, List<Integer>> {

  private final List<Integer> solutionResult = new ArrayList<>();
  private final List<Integer> checkResult = new ArrayList<>();

  public static void main(String[] args) {
    TX11_001 instance = new TX11_001();
    instance.methodTemplate();
  }

  @Override
  protected BinaryTree randomParam1() {
    return BinaryTree.generateBinaryTree(5, 5);
  }

  @Override
  protected List<Integer> solution(BinaryTree param1) {
    if (param1 == null) {
      return solutionResult;
    }
    LinkedList<BinaryTree> list = new LinkedList<>();
    BinaryTree cur = param1;
    list.offer(param1);
    while (!list.isEmpty()) {
      cur = list.poll();
      solutionResult.add(cur.getValue());
      if (cur.getLeftNode() != null) {
        list.offer(cur.getLeftNode());
      }
      if (cur.getRightNode() != null) {
        list.offer(cur.getRightNode());
      }
    }
    return solutionResult;
  }

  @Override
  protected List<Integer> check(BinaryTree param1) {
    if (param1 == null) {
      return solutionResult;
    }
    List<List<BinaryTree>> table = new ArrayList<>();
    table.add(Collections.singletonList(param1));

    int index = 0;
    int finalIndex = 1;
    while (index < finalIndex) {
      List<BinaryTree> next = new ArrayList<>();
      List<BinaryTree> cur = table.get(index);
      for (BinaryTree binaryTree : cur) {
        if (binaryTree.getLeftNode() != null) {
          next.add(binaryTree.getLeftNode());
        }
        if (binaryTree.getRightNode() != null) {
          next.add(binaryTree.getRightNode());
        }
      }
      if (!next.isEmpty()) {
        finalIndex++;
        table.add(next);
      }
      index++;
    }
    for (List<BinaryTree> list : table) {
      for (BinaryTree binaryTree : list) {
        checkResult.add(binaryTree.getValue());
      }
    }
    return checkResult;
  }
}
