package com.example.administrator.read.util;

/**
 * Created by Administrator on 2017/11/14.
 */

public class DoubleClickExit {

    public static long mLastClick = 0L;
    private static final int THRESHOLD = 2000;

    public static boolean check() {
        long now = System.currentTimeMillis();
        boolean b = now - mLastClick < THRESHOLD;
        mLastClick = now;
        return b;
    }
}