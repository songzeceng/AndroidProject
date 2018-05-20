package com.example.songzeceng.myndkdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.songzeceng.myndkdemo.model.Person;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String s = MyNdkUtil.getAppKey("I love you son");
        Log.i(TAG, s);

        Person p = new Person("Jason", 24);
        MyNdkUtil.updatePersonInfo(p);
        Log.i(TAG, p.toString());

    }
}
