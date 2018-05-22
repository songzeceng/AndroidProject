package com.example.songzeceng.fileencryptanddecryptndk;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.security.KeyManagementException;
import java.security.Permission;
import java.security.Permissions;

public class MainActivity extends Activity {
    public static final String TAG = "MainActivity";
    private Button btn_encrypt, btn_decrypt;
    private String filePath = Environment.getExternalStorageDirectory() + "/new.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_encrypt = (Button)findViewById(R.id.btn_encrypt);
        btn_decrypt = (Button)findViewById(R.id.btn_decrypt);

        btn_encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "待加密文件路径:"+filePath);
                if (!TextUtils.isEmpty(filePath) && new File(filePath).exists()) {
                    filePath = FileUtil.fileEncrypt(filePath);
                }
            }
        });

        btn_decrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "待解密文件路径:"+filePath);
                if (!TextUtils.isEmpty(filePath) && new File(filePath).exists()) {
                    filePath = FileUtil.fileDecrypt(filePath);
                }
            }
        });

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            btn_decrypt.setClickable(false);
            btn_encrypt.setClickable(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED) {
                btn_decrypt.setClickable(true);
                btn_encrypt.setClickable(true);
            }
        }
    }
}
