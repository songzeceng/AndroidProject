package com.example.songzeceng.studyofretrofit.recyclerView;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
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
    private static final String TAG = "MyViewHolderForImage";
    private static int measure_count = 0;
    @BindView(R.id.iv_img) ImageView imageView;
    @BindView(R.id.tv_img) TextView textView;
    private Button button = null;


    private ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            int tv_width = textView.getMeasuredWidth();
            int tv_height = textView.getMeasuredHeight();
            int iv_width = imageView.getMeasuredWidth();
            int iv_height = imageView.getMeasuredHeight();

            measure_count++;

            Log.i(TAG,"tv_width:"+tv_width);
            Log.i(TAG,"tv_height:"+tv_height);
            Log.i(TAG,"iv_width:"+iv_width);
            Log.i(TAG,"iv_height:"+iv_height);
            Log.i(TAG,"measure_count:"+measure_count);
            Log.i(TAG, "-------------------");
            // 每个测量了三遍

 //           imageView.getViewTreeObserver().removeOnGlobalLayoutListener(layoutListener);
        }
    };
    public MyViewHolderForImage(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        textView.setVisibility(View.INVISIBLE);
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
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
