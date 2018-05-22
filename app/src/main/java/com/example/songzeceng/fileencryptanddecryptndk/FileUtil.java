package com.example.songzeceng.fileencryptanddecryptndk;

/**
 * Created by songzeceng on 2018/5/22.
 */

public class FileUtil {
    static {
        System.loadLibrary("native-lib");
    }

    public static native String fileEncrypt(String filePath);
    public static native String fileDecrypt(String filePath);
}
