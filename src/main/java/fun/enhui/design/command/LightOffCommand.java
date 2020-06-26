package fun.enhui.design.command;

/**
 * 具体命令-关灯
 *
 * @Author 胡恩会
 * @Date 2020/6/26 21:50
 **/
public class LightOffCommand implements ICommand {
    LightReceiver receiver = new LightReceiver();

    @Override
    public void execute() {
        receiver.off();
    }

    @Override
    public void undo() {
        receiver.on();
    }

}
