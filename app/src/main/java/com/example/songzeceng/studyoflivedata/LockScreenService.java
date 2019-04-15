package com.example.songzeceng.studyoflivedata;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class LockScreenService extends Service {
	LockScreenReceiver mReceiver;
	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_SCREEN_ON); // 接收屏幕亮时的广播
		intentFilter.addAction(Intent.ACTION_USER_PRESENT);
		mReceiver = new LockScreenReceiver();
		registerReceiver(mReceiver, intentFilter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}
}
