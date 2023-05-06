package com.example.test1.hook.hookCase

import android.util.Log

open class ClassA: InterfaceA {
    companion object {
        private const val TAG = "ClassA"
    }

    override fun funA() {
        Log.i(TAG, "funA: in ClassA")
    }
}