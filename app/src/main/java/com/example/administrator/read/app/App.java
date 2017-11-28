package com.example.administrator.read.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2017/11/27.
 */
public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}

