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
                || checkSelfPermission(Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED) {
            BaseUtils.jumpStartInterface(this);
            return;
        }
        sendMessage();
    }

    @SuppressLint("MissingPermission")
    private void sendMessage() {
        mSrcPhoneNumber = mTelephonyManager.getLine1Number();
        System.out.println("本机号码：" + mSrcPhoneNumber);

        mSmsManager.sendTextMessage(mDesPhoneNumber, mSrcPhoneNumber, "hello From AS",
                null, null);
    }
}
