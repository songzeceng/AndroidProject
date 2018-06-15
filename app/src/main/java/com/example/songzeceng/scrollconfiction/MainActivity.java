package com.example.songzeceng.scrollconfiction;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends Activity {
    private MyViewPager mViewPager;
    private MyViewPagerAdapter mAdapterForViewPager;
    private LinkedList<MyListView> listViews = new LinkedList<>();
    private ArrayList<String> dataPerPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = findViewById(R.id.root_viewPager);

        for (int i = 0; i < 5; i++) {
            dataPerPage = new ArrayList<>();
            for (int j = 0; j < 20; j++) {
                dataPerPage.add("第" + (i + 1) + "页，第" + (j + 1) + "条");
            }
            MyListView listView = new MyListView(this);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataPerPage);
            listView.setAdapter(arrayAdapter);
            listViews.add(listView);
        }

        mAdapterForViewPager = new MyViewPagerAdapter(this, listViews);
        mViewPager.setAdapter(mAdapterForViewPager);
        mViewPager.setCurrentItem(100 * listViews.size());
    }
}
