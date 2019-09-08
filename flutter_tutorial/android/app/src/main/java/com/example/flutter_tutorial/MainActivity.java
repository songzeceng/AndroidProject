package com.example.flutter_tutorial;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {
    String mSharedText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneratedPluginRegistrant.registerWith(this);

        mSharedText = "来自安卓MainActivity的问候";

        // 通过methodChannel给Flutter传送数据
        new MethodChannel(getFlutterView(), "app.channel.shared.data")
                .setMethodCallHandler((methodCall, result) -> {
                    System.out.println("method:" + methodCall.method);
                    if (methodCall.method.contentEquals("getSharedText")) {
                        result.success(mSharedText);
                    }
                });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 0);

            }
        }
    }
}
