package com.example.test1.hook.hookCase

import android.util.Log

class ClassWithInner {
    companion object {
        private const val TAG = "ClassInner"
    }

    private val innerObj = ClassInner()

    public fun executeOut() {
        execute(innerObj)
    }

    private fun execute(inner: ClassInner) {
        inner.innerExecute()
    }

    private inner class ClassInner {
        fun innerExecute() {
            Log.i(TAG, "innerExecute ")
        }
    }
}