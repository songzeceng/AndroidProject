package com.example.songzeceng.studyofretrofit;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import org.jetbrains.annotations.Nullable;

/**
 * Created by songzeceng on 2018/2/5.
 */

public class MyButton extends Button implements View.OnTouchListener{
    public static final String TAG = "MyButton";
    private int count = 0;
    public MyButton(Context context) {
        super(context);
        setOnTouchListener(this);
    }

    public MyButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    public MyButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i(TAG,"MyButton--dispatchTouchEvent:"+event.getAction());
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG,"MyButton--onTouchEvent:"+event.getAction());
        switch (event.getAction()){
            case 0:
//                if(count == 0){
//                    count++;
//                    return true;
//                }else {
//                    count--;
//                    return super.onTouchEvent(event);
//                }
            case 1:
                return super.onTouchEvent(event);
            case 2:
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i(TAG,"MyButton--onTouch:"+event.getAction());
        return false;
    }
}
