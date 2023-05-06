package com.example.test1.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView

class MyTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val consume = super.onTouchEvent(event)

        Log.i("MyTextView", "onTouchEvent: $event has been consumed: $consume")

        return consume
    }
}