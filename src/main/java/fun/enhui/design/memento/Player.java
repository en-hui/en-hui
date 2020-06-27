package fun.enhui.design.memento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 发起人-玩家
 *
 * @Author 胡恩会
 * @Date 2020/6/27 16:03
 **/
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Player {
    /**
     * 游戏名称
     *
     * @Author: 胡恩会
     * @Date: 2020/6/27 16:07
     **/
    private String gameName;
    /**
     * 关卡
     *
     * @Author: 胡恩会
     * @Date: 2020/6/27 16:07
     **/
    private int checkpoint;
    /**
     * 生命
     *
     * @Author: 胡恩会
     * @Date: 2020/6/27 16:08
     **/
    private int life;

    /**
     * 新建存档
     * @Author: 胡恩会
     * @Date: 2020/6/27 16:13
     **/
    public Memento createMemento(){
        Memento memento = new Memento();
        memento.setCheckpoint(checkpoint);
        memento.setGameName(gameName);
        memento.setLife(life);
        return memento;
    }

    /**
     * 恢复存档
     * @Author: 胡恩会
     * @Date: 2020/6/27 16:13
     **/
    public void restoreMemento(Memento memento){
        checkpoint = memento.getCheckpoint();
        gameName = memento.getGameName();
        life = memento.getLife();
    }
}
