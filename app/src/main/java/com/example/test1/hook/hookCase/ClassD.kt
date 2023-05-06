package com.example.test1.hook.hookCase

import android.util.Log

class ClassD: ClassA(), InterfaceC {
    companion object {
        private const val TAG = "ClassD"
    }

    override fun funA() {
        Log.i(TAG, "funA: in ClassD")
    }

    override fun funB() {
        Log.i(TAG, "funB: in ClassD")
    }

    override fun funC() {
        Log.i(TAG, "funC: in ClassD")
    }
}