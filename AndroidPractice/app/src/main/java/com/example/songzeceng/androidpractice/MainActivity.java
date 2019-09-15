package com.example.songzeceng.androidpractice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    private SmsManager mSmsManager = SmsManager.getDefault();
    private TelephonyManager mTelephonyManager;
    private String mDesPhoneNumber = "15528116901";
    private String mSrcPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if ((checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission("android.permission.READ_PHONE_NUMBERS") != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                || checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_SMS, "android.permission.READ_PHONE_NUMBERS",
                    Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS}, 0);

            return;
        }
        sendMessage();


        Button btn = (Button) findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MainActivity", "onClick: button is clicked");
            }
        });

        BaseUtils.hookOnClick(this, btn); // 要在被hook的方法之后进行hook，否则会被老的方法覆盖
    }

    @SuppressLint("MissingPermission")
    private void sendMessage() {
        mSrcPhoneNumber = mTelephonyManager.getLine1Number();
        System.out.println("本机号码：" + mSrcPhoneNumber);

        mSmsManager.sendTextMessage(mDesPhoneNumber, mSrcPhoneNumber, "hello From AS",
                null, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ((checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission("android.permission.READ_PHONE_NUMBERS") != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                || checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            BaseUtils.jumpStartInterface(this);
        }
    }
}
