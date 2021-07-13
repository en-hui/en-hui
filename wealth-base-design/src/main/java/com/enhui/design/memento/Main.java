package com.enhui.design.memento;

/**
 *
 * @Author 胡恩会
 * @Date 2020/6/27 15:51
 **/
public class Main {
    public static void main(String[] args) {
        // 初始状态
        Player player = new Player("魂斗罗",2,3);
        Caretaker caretaker = new Caretaker();

        // 打 boss 前存档
        player.setLife(1);
        player.setCheckpoint(10);
        caretaker.add("第十关Boss只有一条命存档",player.createMemento());
        System.out.println("关键时刻存档"+player);

        // 闯关失败，恢复存档从新打
        player.setCheckpoint(10);
        player.setLife(0);
        player.restoreMemento(caretaker.get("第十关Boss只有一条命存档"));
        System.out.println("闯关失败，恢复存档"+player);
    }
}
