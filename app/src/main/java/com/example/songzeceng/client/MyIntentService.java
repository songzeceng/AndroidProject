package com.example.songzeceng.client;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by 123 on 2018/11/23.
 */

public class MyIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String threadName = Thread.currentThread().getName();
        System.out.println("onHandleIntent所在的线程：" + threadName);
    }
}
