package com.example.songzeceng.studyofretrofit.recyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.songzeceng.studyofretrofit.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by songzeceng on 2018/2/7.
 */

public class MyViewHolderForImage extends RecyclerView.ViewHolder {
    @BindView(R.id.iv_img) ImageView imageView;
    @BindView(R.id.tv_img) TextView textView;
    private Button button = null;
    public MyViewHolderForImage(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        textView.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.btn_img)
    public void changeVisibility(){
        textView.setVisibility(View.VISIBLE);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getTextView() {
        return textView;
    }
}
