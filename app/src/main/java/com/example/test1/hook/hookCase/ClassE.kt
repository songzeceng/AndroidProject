package com.example.test1.hook.hookCase

import android.util.Log

class ClassE: ClassC() {
    companion object {
        private const val TAG = "ClassE"
    }

    override fun funA() {
        Log.i(TAG, "funA: in ClassE")
    }

    override fun funB() {
        Log.i(TAG, "funB: in ClassE")
    }
}