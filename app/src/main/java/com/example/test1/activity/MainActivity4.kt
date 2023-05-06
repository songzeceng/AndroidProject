package com.example.test1.activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.example.test1.hook.hookCase.*

class MainActivity4: Activity() {
    companion object {
        const val TAG = "MainActivity4"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: ")
        Log.d(TAG, "onCreate: ")
        val a = ClassA()
        a.funA()

        val b = ClassB()
        b.funA()

        val c = ClassC()
        c.funA()
        c.funB()

        val d = ClassD()
        d.funA()
        d.funB()
        d.funC()

        val e = ClassE()
        e.funA()
        e.funB()

        val classWithInner = ClassWithInner()
        classWithInner.executeOut()

        val exceptionThrow = ExceptionThrow()
        exceptionThrow.test()

        val classWithField = ClassWithField()
        classWithField.print()
    }
}