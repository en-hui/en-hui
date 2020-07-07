package fun.enhui.design.combination;

import lombok.Data;

/**
 * 产品节点
 *
 * @Author 胡恩会
 * @Date 2020/7/7 20:21
 **/
@Data
public class NodeProduct extends BaseNode {
    public NodeProduct(BaseObject object) {
        super(object);
    }
}
