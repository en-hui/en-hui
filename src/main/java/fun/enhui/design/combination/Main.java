package fun.enhui.design.combination;

import java.util.*;

/**
 * 组合模式
 * @Author 胡恩会
 * @Date 2020/7/7 20:08
 **/
public class Main {
    public static void main(String[] args) {
        // 构造模拟数据
        List<BaseObject> objects = initData();

        List<BaseNode> results = new ArrayList<>();
        // key为父节点id 值为子节点
        Map<String,List<BaseNode>> collection = new HashMap<>(16);
        for (int i=0;i<objects.size();i++){
            BaseNode node;
            List<BaseNode> children = collection.get(objects.get(i).getParentId());
            if (children == null){
                children = new ArrayList<>();
            }
            if (objects.get(i).getIsDirectory()){
                node = new NodeDirectory(objects.get(i));
            }else{
                node = new NodeProduct(objects.get(i));
            }
            children.add(node);
            collection.put(objects.get(i).getParentId(),children);
            results.add(node);
        }
        Iterator<BaseNode> iterator = results.iterator();
        while(iterator.hasNext()){
            BaseNode baseNode = iterator.next();
            if (baseNode instanceof NodeDirectory){
                NodeDirectory node = (NodeDirectory)baseNode;
                List<BaseNode> baseNodes = collection.get(node.object.getUid());
                node.children = baseNodes;
            }
            if (baseNode.object.getParentId() != null){
                iterator.remove();
            }
        }
        print(results);
    }

    /**
     * 打印数据-数图形式
     * @Author: 胡恩会
     * @Date: 2020/7/7 21:10
    * @param results:
    * @return: void
     **/
    private static void print(List<BaseNode> results) {

    }

    /**
     * 构造模拟数据
     * @Author: 胡恩会
     * @Date: 2020/7/7 21:10
    * @return: java.util.List<fun.enhui.design.combination.BaseObject>
     **/
    private static List<BaseObject> initData() {
        List<BaseObject> objects = new ArrayList<>();
        BaseObject o1 = new BaseObject(null,"1","个人",true);
        BaseObject o2 = new BaseObject(null,"2","企业",true);
        BaseObject o3 = new BaseObject("1","3","个人经营类贷款",true);
        BaseObject o4 = new BaseObject("1","4","个人消费类贷款",true);
        BaseObject o5 = new BaseObject("3","5","存单质押贷款",false);
        BaseObject o6 = new BaseObject("4","6","公务员担保贷款",false);
        BaseObject o7 = new BaseObject("4","7","个人汽车按揭贷款",false);
        objects.add(o1);
        objects.add(o2);
        objects.add(o3);
        objects.add(o4);
        objects.add(o5);
        objects.add(o6);
        objects.add(o7);
        return objects;

    }
}
