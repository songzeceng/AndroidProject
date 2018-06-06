package com.example.songzeceng.studyofretrofit;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by songzeceng on 2018/4/24.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        String path = getApplicationContext().getCacheDir().getAbsolutePath() + "/my-cache/Bayern/Muller";
        Log.i("MyApplication",path);

        File file = new File(path);

        while (!file.exists()) {
            file.mkdirs();
        }

        ///data/data/com.example.songzeceng.studyofretrofit/cache
        // /data/data/com.example.songzeceng.studyofretrofit/cache/my-cache/Bayern/Muller
    }
}
