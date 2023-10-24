package com.enhui.design.observer.day20231024;


import java.util.Observable;

public class Child extends Observable {
    private String name = "孩子王：哈哈";

  public void isCry() {
    System.out.println("孩子哭了");
    super.setChanged();
    this.notifyObservers("孩子哭了");
  }

  public void isSleep() {
    System.out.println("孩子睡了");
    super.setChanged();
    this.notifyObservers("孩子睡了");
  }

    @Override
    public String toString() {
        return "Child{" +
                "name='" + name + '\'' +
                '}';
    }
}
