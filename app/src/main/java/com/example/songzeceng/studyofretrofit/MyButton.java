package com.example.songzeceng.studyofretrofit;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Scroller;

import org.jetbrains.annotations.Nullable;

/**
 * Created by songzeceng on 2018/2/5.
 */

public class MyButton extends Button {
    public static final String TAG = "MyButton";
    private Scroller mScroller;

    public MyButton(Context context) {
        this(context, null);
    }

    public MyButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int downOffset = mScroller.getCurrY() - 0;
                mScroller.startScroll(getScrollX(), getScrollY(), 0, 40 - downOffset);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                int upOffset = mScroller.getCurrY() - 40;
                mScroller.startScroll(getScrollX(), getScrollY(), 0, -40 - upOffset);
                invalidate();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }
}
