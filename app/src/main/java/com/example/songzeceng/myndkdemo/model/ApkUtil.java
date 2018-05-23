package com.example.songzeceng.myndkdemo.model;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by songzeceng on 2018/5/22.
 */

public class ApkUtil {
    public static String getApkPath(Context context) {
        return context.getApplicationContext().getApplicationInfo().sourceDir;
    }

    public static void installApk(Context context, String apkPath) {
        File file = new File(apkPath);
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(FileProvider.getUriForFile(context, "com.example.songzeceng.myndkdemo.myFileProvider", file), "application/vnd.android.package-archive");
            context.startActivity(intent);
        }
    }
}
