package com.example.songzeceng.studyoflivedata;

import android.app.Activity;

public class ActivityManager {
	private static Activity mCurrentActivity;

	public static Activity getCurrentActivity() {
		return mCurrentActivity;
	}

	public static void setCurrentActivity(Activity currentActivity) {
		mCurrentActivity = currentActivity;
	}
}
