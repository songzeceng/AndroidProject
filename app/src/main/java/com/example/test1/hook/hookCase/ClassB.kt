package com.example.test1.hook.hookCase

import android.util.Log

class ClassB: ClassA() {
    companion object {
        private const val TAG = "ClassB"
    }

    override fun funA() {
        Log.i(TAG, "funA: in ClassB")
    }
}