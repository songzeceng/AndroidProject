package com.example.test1.utils

import android.content.res.Resources
import android.util.DisplayMetrics

object ScreenUtils {
    private val displayMetrics: DisplayMetrics by lazy {
        Resources.getSystem().displayMetrics
    }

    fun getScreenWidth(): Int = displayMetrics.widthPixels

    fun getScreenHeight(): Int = displayMetrics.heightPixels

    fun getScreenDensityDpi(): Float = displayMetrics.density

}