package com.example.songzeceng.studyoflivedata;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class LockScreenListView extends ListView {
	private float mStartX;
	private int mWindowWidth;
	private Activity mActivity;

	public LockScreenListView(Context context) {
		super(context);
	}

	public LockScreenListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LockScreenListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}


	public void setmWindowWidth(int mWindowWidth) {
		this.mWindowWidth = mWindowWidth;
	}

	public void setActivity(Activity activity) {
		this.mActivity = activity;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		final float x = ev.getX();
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mStartX = x;
				break;
			case MotionEvent.ACTION_MOVE:
				moveContent(x);
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				handleTouchResult(x);
				break;
		}
		return super.onTouchEvent(ev);
	}

	private void moveContent(float x) {
		float offsetX = x - mStartX;
		if (offsetX < 0f) {
			offsetX = 0f;
		}
		setTranslationX(offsetX); // 内容的偏移量
	}

	private void handleTouchResult(float destination) {
		float offsetX = destination - mStartX;
		if (offsetX > mWindowWidth * 0.4) { // 超过阈值，结束锁屏activity
			handleTouchResult(mWindowWidth - this.getLeft(), true);
		} else { // 否则内容回到原位
			handleTouchResult(-getLeft(), false);
		}
	}

	private void handleTouchResult(float destination, boolean finishActivity) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(this, "translationX", destination);
		animator.setDuration(250).start();
		if (finishActivity) {
			animator.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					super.onAnimationEnd(animation);
					mActivity.finish();
				}
			});
		}
	}

}
