package com.example.songzeceng.studyofretrofit.recyclerView;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.songzeceng.studyofretrofit.R;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;

/**
 * Created by songzeceng on 2018/2/7.
 */

public class AdapterForRecyclerVIew extends RecyclerView.Adapter {
    private enum viewType {
        TYPE_TEXT, TYPE_IMAGE;
    }

    private LinkedList<String> urls = new LinkedList<>();
    private LinkedList<String> descriptions = new LinkedList<>();
    private Context context = null;

    private String TAG = "AdapterForRecyclerVIew";

    public AdapterForRecyclerVIew(LinkedList<String> urls,LinkedList<String> descriptions, Context context) {
        this.urls = urls;
        this.descriptions = descriptions;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == urls.size() + 1) {
            return viewType.TYPE_TEXT.ordinal();
        }
        return viewType.TYPE_IMAGE.ordinal();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == AdapterForRecyclerVIew.viewType.TYPE_IMAGE.ordinal()) {
            CardView layout = (CardView) LayoutInflater.from(context).inflate(R.layout.item_recycler, parent, false);
            return new MyViewHolderForImage(layout);
        } else {
            LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.text_item_recycler, parent, false);
            return new MyViewHolderForText(layout);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position > 0 && position < urls.size() + 1) {
            MyViewHolderForImage holderForImage = (MyViewHolderForImage) holder;
            Picasso.with(context).load(urls.get(position - 1)).into(holderForImage.getImageView());
            holderForImage.getTextView().setText(descriptions.get(position - 1));
        } else {
            ((MyViewHolderForText) holder).getTextView().setText("宋泽嶒");
        }
    }

    @Override
    public int getItemCount() {
        return urls.size() + 2;
    }
}
