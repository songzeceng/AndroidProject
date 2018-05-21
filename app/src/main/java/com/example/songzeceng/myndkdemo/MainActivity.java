package com.example.songzeceng.myndkdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.songzeceng.myndkdemo.model.Man;
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

        Person p2 = MyNdkUtil.getPerson(30, p);
        Log.i(TAG, p2.toString());

        p2 = MyNdkUtil.getPerson2(p2, 29);
        Log.i(TAG, p2.toString());

        Man m = new Man("Mike", 19);
        MyNdkUtil.callSuper(m);
    }
}
