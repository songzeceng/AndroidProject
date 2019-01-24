package com.example.songzeceng.firstjd.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

public class AutoTextureView extends TextureView {
	private int ratioWidth = 0;
	private int ratioHeight = 0;

	public AutoTextureView(final Context context) {
		this(context, null);
	}

	public AutoTextureView(final Context context, final AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AutoTextureView(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setAspectRatio(final int width, final int height) {
		if (width < 0 || height < 0) {
			throw new IllegalArgumentException("Size cannot be negative.");
		}
		ratioWidth = width;
		ratioHeight = height;
		requestLayout();
	}

	@Override
	protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int height = MeasureSpec.getSize(heightMeasureSpec);
		if (0 == ratioWidth || 0 == ratioHeight) {
			setMeasuredDimension(width, height);
		} else {
			setMeasuredDimension(width, width * ratioHeight / ratioWidth);
		}
	}

}
