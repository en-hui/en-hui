package fun.enhui.design.command;

/**
 * 抽象命令角色
 *
 * @Author 胡恩会
 * @Date 2020/6/26 21:38
 **/
public interface ICommand {
    void execute();
    void undo();
}
