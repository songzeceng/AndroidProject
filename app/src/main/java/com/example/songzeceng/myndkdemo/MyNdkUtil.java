package com.example.songzeceng.myndkdemo;

import com.example.songzeceng.myndkdemo.model.Man;
import com.example.songzeceng.myndkdemo.model.Person;

/**
 * Created by songzeceng on 2018/5/18.
 */

public class MyNdkUtil {
    private static final String LIB = "native-lib";
    static {
        System.loadLibrary(LIB);
    }

    public static native String getAppKey(String s);
    public static native void updatePersonInfo(Person p);
    public static native Person getPerson(int age, Person p);
    public static native Person getPerson2(Person p, int age);
    public static native void callSuper(Man m);
}
