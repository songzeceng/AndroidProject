package com.example.songzeceng.scrollconfiction;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by songzeceng on 2018/6/14.
 */

public class MyViewPager extends ViewPager {
    public static final String TAG = "MyViewPager";
    private int mLastDownX;
    private int mLastDownY;
    private boolean mNext = false;
    private int mCurrPos = 0;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastDownX = (int) ev.getX();
                mLastDownY = (int) ev.getY();
                return false;
            case MotionEvent.ACTION_MOVE:
                int newX = (int) ev.getX();
                int newY = (int) ev.getY();

                int deltaX = newX - mLastDownX;
                int deltaY = newY - mLastDownY;

                boolean shouldIntercept = Math.abs(deltaX) - Math.abs(deltaY) > 15;

                if (shouldIntercept) {
                    mNext = deltaX < 0;
                }

                return shouldIntercept;
            case MotionEvent.ACTION_UP:
                return false;

        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                mCurrPos = mNext ? getCurrentItem() + 1 : getCurrentItem() - 1;
                int itemCount = getAdapter().getCount();
                if (mCurrPos >= itemCount) {
                    mCurrPos = 0;
                } else if (mCurrPos < 0) {
                    break;
                }
                setCurrentItem(mCurrPos, true);
                break;

        }
        return super.onTouchEvent(ev);
    }
}
