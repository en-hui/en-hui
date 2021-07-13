package com.enhui.algorithm.左神算法.基础班.day05;

import java.util.HashMap;

/**
 * 前缀树
 *
 * @author 胡恩会
 * @date 2021/1/5 20:47
 */
public class Code01_TrieTree {

    public static class Node1 {
        int pass = 0;

        int end = 0;

        // 用0~25 表示 a~z
        Node1[] nexts;

        public Node1() {
            nexts = new Node1[26];
        }
    }

    /**
     * 如果字符串只有 a-z 可以用这种
     * 每次提前创建好26个next
     **/
    public static class TrieTree1 {
        // 头节点
        public Node1 root;

        public TrieTree1() {
            root = new Node1();
        }

        /**
         * 插入一个字符串到前缀树中
         **/
        public void insert(String word) {
            if (word == null) {
                return;
            }
            char[] chars = word.toCharArray();
            Node1 node = root;
            node.pass++;
            int path = 0;
            for (int i = 0; i < chars.length; i++) {
                // 表示走哪条路径
                path = chars[i] - 'a';
                // 表示这条路径不存在,就创建出来
                if (node.nexts[path] == null) {
                    node.nexts[path] = new Node1();
                }
                node = node.nexts[path];
                node.pass++;
            }
            // 修改最后一个节点的end
            node.end++;
        }

        /**
         * 查找一个字符串在前缀树中被添加的次数
         **/
        public int search(String word) {
            if (word == null) {
                return 0;
            }
            Node1 node = root;
            char[] chars = word.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                int path = chars[i] - 'a';
                // 没找到
                if (node.nexts[path] == null) {
                    return 0;
                }
                node = node.nexts[path];
            }
            return node.end;
        }

        /**
         * 从前缀树中删除一个字符串
         * 如果不存在，直接返回
         * 如果删到中途，发现删完之后pass变0了，后面节点直接舍弃
         * 如果删到最后了，最后一个节点end-1
         **/
        public void delete(String word) {
            if (search(word) == 0) {
                return;
            }
            Node1 node = root;
            node.pass--;
            char[] chars = word.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                int path = chars[i] - 'a';
                // 如果某个节点的pass -- 变成0了，下面的节点没必要看了
                if (--node.nexts[path].pass == 0) {
                    node.nexts[path] = null;
                    return;
                }
                node = node.nexts[path];
            }
            node.end--;
        }

        /**
         * 所有加入的字符串中，有多少个是以pre开头的
         **/
        public int prefixNumber(String pre) {
            if (pre == null) {
                return 0;
            }
            char[] chars = pre.toCharArray();
            Node1 node = root;
            for (int i = 0; i < chars.length; i++) {
                int path = chars[i] - 'a';
                // 发现往下找都不存在了，说明没有
                if (node.nexts[path] == null) {
                    return 0;
                }
                node = node.nexts[path];
            }
            return node.pass;
        }
    }

    /**
     * 当字符串中字符多时，上面那种写法就不适合了，Node[26]不够用，也不知道多少够用
     * 就用HashMap来存nexts
     **/
    public static class Node2 {
        int pass;

        int end;

        // 当字符串中字符很多时，用ASCII码做key
        HashMap<Integer, Node2> nexts;

        public Node2() {
            pass = 0;
            end = 0;
            nexts = new HashMap<>();
        }
    }

    public static class TrieTree2 {
        Node2 root;

        public TrieTree2() {
            root = new Node2();
        }
    }

}
