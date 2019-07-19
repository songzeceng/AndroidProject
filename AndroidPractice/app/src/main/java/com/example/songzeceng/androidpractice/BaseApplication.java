package com.example.songzeceng.androidpractice;

import android.app.Application;
import android.content.IntentFilter;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(new SmsReceiver(),
                new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }
}
