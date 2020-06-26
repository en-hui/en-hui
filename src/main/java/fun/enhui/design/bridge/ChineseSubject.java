package fun.enhui.design.bridge;

/**
 * 扩展抽象化角色-语文
 *
 * @Author 胡恩会
 * @Date 2020/6/26 16:43
 **/
public class ChineseSubject extends Subject{
    public ChineseSubject(Grade grade) {
        super(grade);
    }

    @Override
    public String getSubjectName() {
        return grade.getGradeName()+"语文";
    }
}
