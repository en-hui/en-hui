package fun.enhui.thread.pool;

import java.util.concurrent.Executor;

/**
 * Executor 接口
 *
 * @Author 胡恩会
 * @Date 2020/8/27 22:11
 **/
public class T01_MyExecutor implements Executor {
    public static void main(String[] args) {
        new T01_MyExecutor().execute(() -> {
            System.out.println("实现 Executor 接口");
        });
    }

    @Override
    public void execute(Runnable command) {
        command.run();
    }
}
