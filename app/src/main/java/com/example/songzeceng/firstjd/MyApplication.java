package com.example.songzeceng.firstjd;

import android.app.Application;

import com.yorhp.picturepick.PicturePickUtil;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		PicturePickUtil.init("songzeceng");
	}
}
