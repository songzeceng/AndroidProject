package com.example.test1.utils

import android.os.Build

object RomUtils {
    fun isOnePlus(): Boolean = Build.MANUFACTURER== "OnePlus"
}