package com.example.songzeceng.client;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

public class WeakHandler extends Handler {
    private WeakReference<IHandler> handlerWeakReference;

    public WeakHandler(IHandler handler) {
        handlerWeakReference = new WeakReference<>(handler);
    }

    @Override
    public void handleMessage(Message msg) {
        if (handlerWeakReference == null || handlerWeakReference.get() == null) {
            return;
        }
        handlerWeakReference.get().handleMessage(msg);
    }
}
