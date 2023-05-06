package com.example.test1.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileUtil {
    public static void saveFile(String fileName, String content, Context context) throws Exception {
        if (content == null || TextUtils.isEmpty(fileName) || TextUtils.isEmpty(content)) {
            return;
        }

        File dir = getCacheDir(context);
        if (dir == null) {
            return;
        }
        File file = new File(dir + "/" + fileName);
        if (!file.exists() && !file.createNewFile()) {
            return;
        }

        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
        outputStream.write(content.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }

    private static File getCacheDir(Context context) {
        if (context == null) {
            return null;
        }

       return context.getExternalCacheDir() != null ? context.getExternalCacheDir() : context.getCacheDir();
    }


}
