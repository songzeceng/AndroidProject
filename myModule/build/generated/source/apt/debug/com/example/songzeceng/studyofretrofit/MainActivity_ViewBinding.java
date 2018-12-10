// Generated code from Butter Knife. Do not modify!
package com.example.songzeceng.studyofretrofit;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  private View view2131492873;

  private View view2131492875;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(final MainActivity target, View source) {
    this.target = target;

    View view;
    target.tv_show = Utils.findRequiredViewAsType(source, R.id.tv_show, "field 'tv_show'", TextView.class);
    target.et_input = Utils.findRequiredViewAsType(source, R.id.et_input, "field 'et_input'", EditText.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.rv_recycler, "field 'recyclerView'", RecyclerView.class);
    target.iv_test = Utils.findRequiredViewAsType(source, R.id.iv_test, "field 'iv_test'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.btn_confirm, "method 'translate'");
    view2131492873 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.translate(Utils.<Button>castParam(p0, "doClick", 0, "translate", 0));
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_share, "method 'share'");
    view2131492875 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.share();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tv_show = null;
    target.et_input = null;
    target.recyclerView = null;
    target.iv_test = null;

    view2131492873.setOnClickListener(null);
    view2131492873 = null;
    view2131492875.setOnClickListener(null);
    view2131492875 = null;
  }
}
