package com.example.songzeceng.myndkdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.songzeceng.myndkdemo.model.ApkUtil;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String STORAGE_PATH = Environment.getExternalStorageDirectory() + "/";
    public static final String PATCH_FILE = "my.patch";
    public static final String NEW_APK_FILE = "new.apk";

    private Button btn_click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_click = (Button) findViewById(R.id.btn_click);
        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadUtil.executeRunnable(new Runnable() {
                    @Override
                    public void run() {
                        String oldApkPath = ApkUtil.getApkPath(MainActivity.this);
                        File oldApkFile = new File(oldApkPath);
                        File patchFile = new File(STORAGE_PATH + PATCH_FILE);

                        if (oldApkFile.exists() && patchFile.exists()) {
                            int ret = MyNdkUtil.patch(oldApkPath, STORAGE_PATH + NEW_APK_FILE, STORAGE_PATH + PATCH_FILE);
                            Log.i(TAG, "patch 结果:"+ret);
                        }

                        if (new File(STORAGE_PATH + NEW_APK_FILE ).exists()) {
                            ApkUtil.installApk(MainActivity.this, STORAGE_PATH + NEW_APK_FILE);
                        }
                    }
                });
            }
        });

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            btn_click.setClickable(false);
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            boolean pass = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    pass = false;
                    break;
                }
            }

            btn_click.setClickable(pass);
        }
    }
}
