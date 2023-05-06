package com.example.test1.hook.hookCase

import android.util.Log

class ClassWithField {
    companion object {
        const val TAG = "ClassWithField"
    }

    var field0: Int = 0
    private var field1: String = ""

    override fun toString(): String {
        return "ClassWithField{field0=$field0, field1=$field1}"
    }

    fun print() {
        Log.i(TAG, "print: ${toString()}")
    }
}