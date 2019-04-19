package com.example.songzeceng.studyoflivedata;

import android.app.PendingIntent;
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
			case Intent.ACTION_CLOSE_SYSTEM_DIALOGS:
				String reason = intent.getStringExtra("reason");
				Log.i("LockScreen", "onReceive: reason:" + reason);
				switch (reason) {
					case "homekey":
						if (ActivityManager.getCurrentActivity() != null &&
								ActivityManager.getCurrentActivity() instanceof LockScreentActivity) {
							Intent newIntent = new Intent(context, LockScreentActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);       //点击home键无延时，且不会产生新的activity
							PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
									newIntent, 0);
							try {
								pendingIntent.send();
							} catch (PendingIntent.CanceledException e) {
								e.printStackTrace();
							}
							return;
						}
						break;
				}
				break;
		}
	}
}
