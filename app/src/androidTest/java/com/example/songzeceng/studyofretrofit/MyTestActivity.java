package com.example.songzeceng.studyofretrofit;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by songzeceng on 2018/6/25.
 */

public class MyTestActivity extends ActivityInstrumentationTestCase2<MainActivity> {
    private Context context;

    public MyTestActivity() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getActivity().getApplicationContext(); // 防止内存泄漏
    }

    public void testStart() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
