package com.example.songzeceng.studyoflivedata;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import java.util.LinkedList;

/**
 * 锁屏界面，由自定义Service启动
 * 主要逻辑：1、设置window属性
 *           2、处理滑屏事件（在内容view中处理）
 * 处理滑屏事件主要逻辑：1、按下事件，记录横坐标
 *                       2、移动事件，根据横坐标和按下时的横坐标，决定内容的偏移量
 *                       3、抬起事件，根据横坐标，计算滑动了多远，超过阈值，则结束锁屏界面；否则，内容归位
 */
public class LockScreentActivity extends Activity {
	private LockScreenListView mContentView;
	private int mWindowWidth = 0;

	private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver
			.OnGlobalLayoutListener() {
		@Override
		public void onGlobalLayout() {
			mWindowWidth = getWindow().getDecorView().getWidth();
			mContentView.setmWindowWidth(mWindowWidth);
			mContentView.setActivity(LockScreentActivity.this);
			if (mContentView != null) {
				mContentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
			}
		}
	};

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initWindow();

		setContentView(R.layout.activity_screen_lock);

		mContentView = findViewById(R.id.lock_content);
		int statusHeight = getStatusBarHeight();
		Log.i("LockScreen", "onCreate: 状态栏高度:" + statusHeight);
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mContentView.getLayoutParams();
		layoutParams.topMargin = statusHeight + 5;
		mContentView.setLayoutParams(layoutParams);
		initContentData();

		mContentView.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
	}

	private void initContentData() {
		LinkedList<String> linkedList = new LinkedList<>();
		linkedList.add("a");
		linkedList.add("b");
		linkedList.add("c");
		linkedList.add("d");
		linkedList.add("e");

		MyAdapter adapter = new MyAdapter(linkedList, this);
		mContentView.setAdapter(adapter);
	}

	private void initWindow() {
		final Window window = getWindow();
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD // 取消系统锁屏
				| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED); // 锁屏时仍显示
		window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE // 防止系统栏隐藏时activity大小发生变化
							| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY // 沉浸式
							| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION // 隐藏导航栏
							| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // 隐藏导航栏
							| View.SYSTEM_UI_FLAG_FULLSCREEN
							| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN); // 全屏
		window.setNavigationBarColor(Color.TRANSPARENT);
		window.setStatusBarColor(Color.TRANSPARENT);

		// 设置标题栏透明
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		} else {
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(0);
		}
	}

	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	@Override
	public void onBackPressed() {
		// 屏蔽返回键
	}
}
