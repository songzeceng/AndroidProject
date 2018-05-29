package com.example.songzeceng.studyoflivedata;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.LinkedList;

public class MainActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("a");
        linkedList.add("b");
        linkedList.add("c");
        linkedList.add("d");
        linkedList.add("e");

        MyAdapter adapter = new MyAdapter(linkedList, this);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        LinearLayout header = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.my_header, listView, false);
//        AbsListView.LayoutParams params = (AbsListView.LayoutParams) header.getLayoutParams(); // 父view则是Abs
//        params.height = AbsListView.LayoutParams.WRAP_CONTENT;
        LinearLayout btns = header.findViewById(R.id.btns);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT); // 类型是父view的布局参数类型
        params2.width = AbsListView.LayoutParams.WRAP_CONTENT;
        btns.setLayoutParams(params2);

        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);// 类型是父view的布局参数类型
        params3.height = AbsListView.LayoutParams.WRAP_CONTENT;
        btns.findViewById(R.id.btn1).setLayoutParams(params3);


        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setText("123455");
        textView.setLayoutParams(params4);
        header.addView(textView);
        listView.addHeaderView(header);


        // Relative/LinearLayout.LayoutParams不能转换成AbsListView.LayoutParams
        //

//        MainFragment fragment = new MainFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, fragment).commit();
        //getSupportFragmentManager()只能在FragmentActivity或AppCompatActivity中调用
    }
}
