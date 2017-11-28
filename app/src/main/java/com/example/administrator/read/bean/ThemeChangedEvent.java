package com.example.administrator.read.bean;

/**
 * Created by Administrator on 2017/11/28.
 */

public class ThemeChangedEvent {

    public final  int message;

    public ThemeChangedEvent(int message) {
        this.message = message;
    }

    public int getMsg() {
        return message;
    }



}
