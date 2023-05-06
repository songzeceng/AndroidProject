package com.example.test1.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;

public class RadiusLayout extends FrameLayout {
    private final float[] mRadius = new float[8];
//    private final Path path = new Path();

    public RadiusLayout(@NonNull Context context) {
        this(context,null);
    }

    public RadiusLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public void setRadius(float radius) {
        Arrays.fill(mRadius, radius);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
//        canvas.save();
        float leftTopRadius = mRadius[0];
        float rightTopRadius = mRadius[2];
        float rightBottomRadius = mRadius[4];
        float leftBottomRadius = mRadius[6];
        RectF rectF = new RectF(0, 0, getWidth(), getHeight());
        Path path = new Path();
//        path.reset();
        path.addRoundRect(rectF, mRadius, Path.Direction.CW);
//        path.addRoundRect(rectF, mRadius, Path.Direction.CW);
        canvas.clipPath(path);
//        canvas.restore();
        super.dispatchDraw(canvas);
    }
}