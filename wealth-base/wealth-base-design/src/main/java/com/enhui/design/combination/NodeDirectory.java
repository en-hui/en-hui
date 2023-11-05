package com.enhui.design.combination;

import lombok.Data;

import java.util.List;

/**
 * 目录节点
 *
 * @Author 胡恩会
 * @Date 2020/7/7 20:21
 **/
@Data
public class NodeDirectory extends BaseNode{
    List<BaseNode> children;

    public NodeDirectory(BaseObject object) {
        super(object);
    }
}
