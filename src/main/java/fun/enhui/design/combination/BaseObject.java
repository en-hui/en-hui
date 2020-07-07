package fun.enhui.design.combination;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author 胡恩会
 * @Date 2020/7/7 19:56
 **/
@Data
@AllArgsConstructor
public class BaseObject {
    private String parentId;
    private String uid;
    private String name;
    private Boolean isDirectory;

}
