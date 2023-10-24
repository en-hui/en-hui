package com.enhui.design.observer.day20231024;

import java.util.Observable;
import java.util.Observer;

public class DogObserver implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        System.out.println(o + " " + arg);
    }
}
