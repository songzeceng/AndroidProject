package com.example.songzeceng.studyofretrofit;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by songzeceng on 2018/6/25.
 */

public class MyTestActivity extends ActivityInstrumentationTestCase2<MainActivity> {
    private Context context;

    public MyTestActivity() { // 不要传参，否则会报错：has no public constructor TestCase(String name) or TestCase()
        super(MainActivity.class); // 传入泛型的class
    }


    public void testStart() {

    }
}
