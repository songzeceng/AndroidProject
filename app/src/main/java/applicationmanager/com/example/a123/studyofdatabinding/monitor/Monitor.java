package applicationmanager.com.example.a123.studyofdatabinding.monitor;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import applicationmanager.com.example.a123.studyofdatabinding.R;
import applicationmanager.com.example.a123.studyofdatabinding.myInterface.IActivity;

public class Monitor {
    private IActivity mActivity;
//    private MyBindingComponentAdapter myBindingComponentAdapter = new ImageViewAdapter();

    public Monitor(IActivity mActivity) {
        this.mActivity = mActivity;
//        myBindingComponentAdapter.setReference(mActivity);
    }

    public void onClick(View view) {
        if (mActivity == null) {
            return;
        }

        int old = mActivity.getData();
        switch(view.getId()){
            case R.id.increment:
                old++;
                break;
            case R.id.decrement:
                old--;
                break;
        }
        mActivity.onDataChanged(old);
    }

    // 绑定方法，对应app:imageUrl
    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext().getApplicationContext()).load(Uri.parse(url)).into(view);
    }

    // 安卓属性setter
    @BindingAdapter("android:layout_marginLeft")
    public static void setLeftMargin(View view, int margin) {
        ViewGroup.LayoutParams layoutParams= view.getLayoutParams();
        if (layoutParams instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) layoutParams).leftMargin = margin + 5;
        } else if (layoutParams instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) layoutParams).leftMargin = margin + 5;
        } else if (layoutParams instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) layoutParams).leftMargin = margin + 5;
        }

        view.setLayoutParams(layoutParams);
    }

//    @Override
//    public MyBindingComponentAdapter getMyBindingComponentAdapter() {
//        return myBindingComponentAdapter;
//    }
}
