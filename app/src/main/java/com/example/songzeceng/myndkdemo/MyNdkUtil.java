package com.example.songzeceng.myndkdemo;

/**
 * Created by songzeceng on 2018/5/18.
 */

public class MyNdkUtil {
    private static final String LIB = "native-lib";
    static {
        System.loadLibrary(LIB);
    }

    public static native String getAppKey(String s);
}
