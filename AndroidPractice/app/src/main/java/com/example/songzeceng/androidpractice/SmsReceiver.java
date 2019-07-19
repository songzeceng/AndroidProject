package com.example.songzeceng.androidpractice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
    private static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(SMS_RECEIVED_ACTION)) {
            System.out.println("收到短信");

            Bundle bundle = intent.getExtras();

            if (bundle != null) {

                Object pdusData[] = (Object[]) bundle.get("pdus");
                // pdus ：protocol data unit  ：
                SmsMessage[] msg = new SmsMessage[pdusData.length];
                for (int i = 0; i < msg.length; i++) {
                    byte pdus[] = (byte[]) pdusData[i];
                    msg[i] = SmsMessage.createFromPdu(pdus);
                }

                StringBuffer content = new StringBuffer();
                StringBuffer phoneNumber = new StringBuffer();
                for (SmsMessage temp : msg) {
                    content.append(temp.getMessageBody());
                    phoneNumber.append(temp.getOriginatingAddress());
                }
                System.out.println("发送者号码：" + phoneNumber.toString()
                        + "  短信内容：" + content.toString());
            }
        }
    }
}
