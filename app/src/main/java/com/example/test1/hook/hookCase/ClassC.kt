package com.example.test1.hook.hookCase

import android.util.Log

open class ClassC: ClassA(), InterfaceB {
    companion object {
        private const val TAG = "ClassC"
    }

    override fun funA() {
        Log.i(TAG, "funA: in ClassC")
    }

    override fun funB() {
        Log.i(TAG, "funB: in ClassC")
    }
}