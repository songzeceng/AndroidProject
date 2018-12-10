// Generated code from Butter Knife. Do not modify!
package com.example.songzeceng.studyofretrofit.recyclerView;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.example.songzeceng.studyofretrofit.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyViewHolderForImage_ViewBinding implements Unbinder {
  private MyViewHolderForImage target;

  private View view2131492874;

  @UiThread
  public MyViewHolderForImage_ViewBinding(final MyViewHolderForImage target, View source) {
    this.target = target;

    View view;
    target.imageView = Utils.findRequiredViewAsType(source, R.id.iv_img, "field 'imageView'", ImageView.class);
    target.textView = Utils.findRequiredViewAsType(source, R.id.tv_img, "field 'textView'", TextView.class);
    view = Utils.findRequiredView(source, R.id.btn_img, "method 'changeVisibility'");
    view2131492874 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.changeVisibility();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    MyViewHolderForImage target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageView = null;
    target.textView = null;

    view2131492874.setOnClickListener(null);
    view2131492874 = null;
  }
}
