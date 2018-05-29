package com.example.songzeceng.studyoflivedata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * Created by songzeceng on 2018/5/29.
 */

public class MyAdapter extends BaseAdapter {
    private LinkedList<String> lists = new LinkedList<>();
    private Context context;

    public MyAdapter(LinkedList<String> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.text);
        textView.setText(lists.get(position));
        return convertView;
    }
}
