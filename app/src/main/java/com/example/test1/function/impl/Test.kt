package com.example.test1.function.impl

import com.example.test1.function.ITest

class Test {
    fun test(iTest: ITest?, s: String?) {
        iTest?.callback(s)
    }
}