// Generated code from Butter Knife. Do not modify!
package com.example.songzeceng.studyofretrofit.recyclerView;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.example.songzeceng.studyofretrofit.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyViewHolderForText_ViewBinding implements Unbinder {
  private MyViewHolderForText target;

  @UiThread
  public MyViewHolderForText_ViewBinding(MyViewHolderForText target, View source) {
    this.target = target;

    target.textView = Utils.findRequiredViewAsType(source, R.id.tv_item, "field 'textView'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MyViewHolderForText target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textView = null;
  }
}
