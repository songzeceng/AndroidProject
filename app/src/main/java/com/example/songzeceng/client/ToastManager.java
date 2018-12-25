package com.example.songzeceng.client;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ToastManager implements IHandler {
    public static final int MSG_SHOW = 0;
    public static final int MSG_HIDE = 1;

    private WeakHandler handler = new WeakHandler(this);
    private String text;
    private View contentView;
    private Drawable drawable;
    private int duration;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private int gravity;
    private Context context;

    public static ToastManager makeText(Context context, String text, int duration) {
        ToastManager toastManager = new ToastManager();
        toastManager.text = text;
        toastManager.context = context.getApplicationContext();
        View contentView = LayoutInflater.from(toastManager.context).inflate(R.layout.toast_layout, null);
        contentView.findViewById(R.id.toast_image).setVisibility(View.GONE);
        toastManager.duration = duration;
        toastManager.contentView = contentView;

        return toastManager;
    }

    public void show() {
        if (contentView == null || text == null || text.isEmpty()) {
            return;
        }
        TextView textView = contentView.findViewById(R.id.toast_text);
        textView.setText(text);
        ImageView imageView = contentView.findViewById(R.id.toast_image);
        imageView.setImageDrawable(drawable);
        imageView.setVisibility(drawable == null ? View.GONE : View.VISIBLE);

        handler.sendEmptyMessage(MSG_SHOW);
    }

    public void setIcon(int resId) {
        if (context == null) {
            return;
        }
        drawable = context.getDrawable(resId);
    }

    public void cancel() {
        handler.removeMessages(MSG_SHOW);
        handler.removeMessages(MSG_HIDE);
        if (windowManager != null && contentView != null) {
            windowManager.removeView(contentView);
        }
    }

    @Override
    public void handleMessage(Message message) {
        switch(message.what){
            case MSG_SHOW:
                if (layoutParams == null) {
                    WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    params.width = WindowManager.LayoutParams.WRAP_CONTENT;
                    params.format = PixelFormat.TRANSLUCENT; // 设置半透明
                    params.windowAnimations = android.R.style.Animation_Toast; // view动画是吐司形式
                    params.type = WindowManager.LayoutParams.TYPE_TOAST; // view的类型是吐司
                    params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE; // view在的时候，保持屏幕亮着，view不可触摸，不可获得焦点
                    params.setTitle("Toast");

                    if (gravity == Gravity.NO_GRAVITY) {
                        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                        params.y = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80,
                                contentView.getContext().getResources().getDisplayMetrics());
                        // 设置view重心和纵向位置
                    } else {
                        params.gravity = gravity;
                    }

                    layoutParams = params;
                }

                windowManager = (WindowManager) contentView.getContext().getSystemService(Context.WINDOW_SERVICE);
                // 获取windowManager
                if (windowManager != null) {
                    windowManager.addView(contentView, layoutParams);
                    // 给window添加view，显示之
                }

                if (duration > 0) {
                    handler.sendEmptyMessageDelayed(MSG_HIDE, duration);
                    // 显示一段时间后让view消失
                }
                break;
            case MSG_HIDE:
                if (contentView != null) {
                    windowManager.removeView(contentView);
                    // 移除view，view就消失了
                }
                break;
        }
    }
}
