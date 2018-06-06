package com.example.songzeceng.studyofretrofit.recyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.songzeceng.studyofretrofit.R;
import com.example.songzeceng.studyofretrofit.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by songzeceng on 2018/2/7.
 */

public class MyViewHolderForText extends RecyclerView.ViewHolder {
    @BindView(R2.id.tv_item) TextView textView;

    public MyViewHolderForText(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public TextView getTextView() {
        return textView;
    }
}
