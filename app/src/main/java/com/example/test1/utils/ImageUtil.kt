package com.example.test1.utils

import android.graphics.Bitmap
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

object ImageUtil {
    fun saveImageBitmap(
        bitmap: Bitmap,
        file: File?,
        quality: Int,
        format: Bitmap.CompressFormat?
    ): Boolean {
        var opRet = false
        if (bitmap.isRecycled) {
            return false
        }
        BufferedOutputStream(FileOutputStream(file)).use {
            bitmap.compress(format, quality, it)
            it.flush()
            opRet = true
        }
        return opRet
    }
}