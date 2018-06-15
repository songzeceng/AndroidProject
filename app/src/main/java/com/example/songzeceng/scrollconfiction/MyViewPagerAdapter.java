package com.example.songzeceng.scrollconfiction;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import java.util.LinkedList;

/**
 * Created by songzeceng on 2018/6/14.
 */

public class MyViewPagerAdapter extends PagerAdapter {
    public static final String TAG = "MyViewPagerAdapter";

    private Context mContext;
    private LinkedList<MyListView> listViews;

    public MyViewPagerAdapter(Context mContext, LinkedList<MyListView> listViews) {
        this.mContext = mContext;
        this.listViews = listViews;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int index = position % listViews.size();
        MyListView itemView = listViews.get(index);

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(listViews.get(position % listViews.size()));
    }
}
