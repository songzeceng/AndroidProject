package com.example.songzeceng.myndkdemo;

import com.example.songzeceng.myndkdemo.model.Man;
import com.example.songzeceng.myndkdemo.model.Person;

/**
 * Created by songzeceng on 2018/5/18.
 */

public class MyNdkUtil {
    private static final String LIB = "bspatch";
    static {
        System.loadLibrary(LIB);
    }

    public static native int patch(String oldApkPath, String newApkPath, String patchPath);

}
