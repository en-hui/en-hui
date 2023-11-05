package com.enhui.algorithm.structure;

import com.enhui.algorithm.util.RandomUtil;
import java.util.ArrayList;
import java.util.List;
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
   * 生成随机二叉树<br>
   * 1、生成随机数，二叉树的节点总数<br>
   * 2、生成头节点，并加到一个list中，这个list表示没有挂满左右子节点的节点<br>
   * 3、随机从list中取出一个节点，随机往他的左或右挂节点，当前节点挂满左右，就从list移除<br>
   * 4、循环3，当节点数达到最大时，程序结束
   *
   * @param maxNodeCount 最大节点个数
   * @param maxValue 节点最大值
   * @return
   */
  public static BinaryTree generateBinaryTree(int maxNodeCount, int maxValue) {
    // 树的最大节点个数
    int nodeCount = RandomUtil.randomJust(maxNodeCount);
    System.out.println("生成随机二叉树，节点个数为：" + nodeCount);
    if (nodeCount == 1) {
      return new BinaryTree(RandomUtil.random(maxValue));
    } else if (nodeCount > 1) {
      BinaryTree head = new BinaryTree(RandomUtil.random(maxValue));
      List<BinaryTree> nodes = new ArrayList<>();
      nodes.add(head);
      int realNodeCount = 1;
      while (realNodeCount < nodeCount) {
        realNodeCount++;
        final BinaryTree curNode = nodes.get(RandomUtil.randomNonnegative(nodes.size() - 1));
        final BinaryTree newNode = new BinaryTree(RandomUtil.random(maxValue));
        if (curNode.getLeftNode() == null && curNode.getRightNode() == null) {
          // 随机往左树或右树挂
          final int leftOrRight = RandomUtil.randomJust(2);
          if (leftOrRight == 1) {
            curNode.setLeftNode(newNode);
          } else {
            curNode.setRightNode(newNode);
          }
        } else if (curNode.getLeftNode() == null) {
          curNode.setLeftNode(newNode);
          nodes.remove(curNode);
        } else {
          curNode.setRightNode(newNode);
          nodes.remove(curNode);
        }
        nodes.add(newNode);
      }
      return head;
    }
    return null;
  }

  public static void printBinaryTree(BinaryTree head) {}

  public static void main(String[] args) {
    final BinaryTree binaryTree = BinaryTree.generateBinaryTree(7, 5);
    System.out.println(binaryTree);
  }
}
