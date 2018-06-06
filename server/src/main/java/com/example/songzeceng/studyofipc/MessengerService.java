package com.example.songzeceng.studyofipc;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;

/**
 * Created by songzeceng on 2018/6/6.
 */

public class MessengerService extends Service {
    private Messenger messenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    try {
                        Bundle content = (Bundle) msg.obj;
                        String info = content.getString("info", "nothing");
                        System.out.println(info);
                        Message replyMesg = Message.obtain();
                        replyMesg.what = 0;
                        content.clear();
                        content.putString("info", "Got it，service pid:" + Process.myPid());
                        replyMesg.obj = content;
                        msg.replyTo.send(replyMesg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    });

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
