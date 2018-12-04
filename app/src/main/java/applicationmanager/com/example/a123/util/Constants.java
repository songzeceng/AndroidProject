package applicationmanager.com.example.a123.util;

import android.os.Environment;

import java.io.File;

public class Constants {
    public static final String TAG = "MainActivity";
    public static final int ERROR_QUEUE_EMPTY = -1;
    public static final int ERROR_NULL_NODE = -2;
    public static final int ERROR_WRONG_TYPE = -3;

    private static final String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String DIR_PATH = SD_PATH + File.separator + "caughtExceptionInfo" + File.separator;
    public static final String PATCH_PATH = DIR_PATH + "bug.apatch";
    public static final String INFO_PATH = DIR_PATH + "crash.txt";
}
