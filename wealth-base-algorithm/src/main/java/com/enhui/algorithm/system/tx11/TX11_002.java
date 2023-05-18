package com.enhui.algorithm.system.tx11;

import com.enhui.algorithm.framework.SingleParamFramework;
import com.enhui.algorithm.structure.BinaryTree;
import java.util.ArrayList;
import java.util.List;

public class TX11_002 extends SingleParamFramework<BinaryTree, BinaryTree> {

  public static void main(String[] args) {
    TX11_002 instance = new TX11_002();
    instance.methodTemplate();
  }

  @Override
  protected BinaryTree randomParam1() {
    return BinaryTree.generateBinaryTree(5, 5);
  }

  @Override
  protected BinaryTree solution(BinaryTree param1) {
    List<String> result = new ArrayList<>();
    // 序列化
    pre(param1, result);

    System.out.println("序列化结果：" + result);
    // 反序列化
    return pre(result);
  }

  private void pre(BinaryTree cur, List<String> result) {
    if (cur == null) {
      result.add("#");
      return;
    }
    result.add(String.valueOf(cur.getValue()));
    pre(cur.getLeftNode(), result);
    pre(cur.getRightNode(), result);
  }

  private BinaryTree pre(List<String> list) {
    if (list.isEmpty()) {
      return null;
    }
    String remove = list.remove(0);
    if (remove.equals("#")) {
      return null;
    } else {
      BinaryTree head = new BinaryTree(Integer.valueOf(remove));
      head.setLeftNode(pre(list));
      head.setRightNode(pre(list));
      return head;
    }
  }

  @Override
  protected BinaryTree check(BinaryTree param1) {
    return param1;
  }
}
