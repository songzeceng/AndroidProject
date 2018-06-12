package com.example.songzeceng.studyofretrofit;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import org.jetbrains.annotations.Nullable;

/**
 * Created by songzeceng on 2018/2/5.
 */

public class MyLinearLayout extends LinearLayout {
    public static final String TAG = "MyLinearLayout";

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.i(TAG,"MyLinearLayout----dispatchTouchEvent:"+ev.getAction());
//        switch (ev.getAction()){
////            case 0:
////                return false;
//            case 1:
//                return super.dispatchTouchEvent(ev);
//            case 2:
//                return super.dispatchTouchEvent(ev);
//        }
        return super.dispatchTouchEvent(ev);
        /*
        dispatchTouchEvent()返回false：
            此事件返回上层控件的onTouch()方法
            此次事件流不再经过这一层和下面的控件
         */
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.i(TAG,"MyLinearLayout----onTouchEvent:"+event.getAction());
//        switch (event.getAction()){
//            case 0:
//                return false;
//            case 1:
//                return true;
//            case 2:
//                return super.onTouchEvent(event);
//        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        Log.i(TAG,"MyLinearLayout--onInterceptTouchEvent:"+ev.getAction());
//        switch (ev.getAction()){
//            case 0:
//                return false;
//            case 1:
//                return super.onInterceptTouchEvent(ev);
//            case 2:
//                return super.onInterceptTouchEvent(ev);
//        }
        return false;
    }
    /*
    onInterceptTouchEvent返回true：此事件和这次事件流的剩余事件，都由自己的onTouch()处理，不会往下传递
     */

}
