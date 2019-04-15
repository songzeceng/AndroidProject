package com.example.songzeceng.studyoflivedata;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LockScreenReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("LockScreen", "onReceive: action:" + intent.getAction());
		switch (intent.getAction()) {
			case Intent.ACTION_SCREEN_ON:
				Intent startIntent = new Intent(context, LockScreentActivity.class);
				startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				context.startActivity(startIntent);
				break;
		}
	}
}
