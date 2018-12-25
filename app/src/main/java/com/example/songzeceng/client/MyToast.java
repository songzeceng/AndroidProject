package com.example.songzeceng.client;

import android.content.Context;

public class MyToast {
    private static final int DEFAULT_DURATION = 1000;

    public static void showToast(Context context, int resId, int duration) {
        String str = context.getApplicationContext().getString(resId);
        showToast(context, str, duration);
    }

    public static void showToast(Context context, String string, int duration) {
        ToastManager manager = ToastManager.makeText(context.getApplicationContext(), string, duration);
        manager.show();
    }

    public static void showToast(Context context, String string) {
        showToast(context, string, DEFAULT_DURATION);
    }

    public static void showToast(Context context, int resId) {
        showToast(context, resId, DEFAULT_DURATION);
    }
}
