package fun.enhui.design.prototype.improve;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Sheep implements Cloneable{
    private String name;
    private int age;
    private String color;

    /**
     * 重写clone
     * @author: HuEnhui
     * @date: 2019/12/22 18:46
     */
    @Override
    protected Object clone(){
        Sheep sheep = null;
        try {
            sheep = (Sheep) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return sheep;
    }
}
