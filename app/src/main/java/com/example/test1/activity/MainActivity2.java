package com.example.test1.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.test1.R;
import com.example.test1.function.impl.Test;
import com.example.test1.utils.AccessibilityHelper;

import java.util.concurrent.FutureTask;

public class MainActivity2 extends Activity {
    private static final String TAG = "MainActivity2";
    private Button mButton1, mButton2;
    private TextView mTextView;
    private ImageView mImageView;
    private FutureTask<Void> mTask;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);
        mButton1 = findViewById(R.id.btn1);
        mButton2 = findViewById(R.id.btn2);
        mTextView = findViewById(R.id.text);
        mImageView = findViewById(R.id.image_2);

        mImageView.setImageDrawable(getDrawable(R.drawable.ic_launcher_background));

        AccessibilityHelper.setAccessibleDelegate(mButton1, "按钮1");
        AccessibilityHelper.setAccessibleDelegate(mButton2, "按钮2");
        AccessibilityHelper.setAccessibleDelegate(mTextView, "文本");
        AccessibilityHelper.setAccessibleDelegate(mImageView, "图片");

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            if (mTask != null) {
                mTask.cancel(true);
            }
            mTask = new FutureTask<>(() -> {
                Thread.sleep((5 - finalI) * 1000L);
                new Test().test(s -> Log.i(TAG, s), "onCreate: i = " + finalI);
                return null;
            });
            AsyncTask.THREAD_POOL_EXECUTOR.execute(mTask);
        }
    }
}