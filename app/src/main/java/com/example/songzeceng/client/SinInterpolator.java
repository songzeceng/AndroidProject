package com.example.songzeceng.client;

import android.view.animation.Interpolator;

public class SinInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float input) {
        return (float) Math.sin(input);
    }
}
