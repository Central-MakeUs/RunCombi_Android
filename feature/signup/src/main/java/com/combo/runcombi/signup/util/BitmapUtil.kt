package com.combo.runcombi.signup.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.core.graphics.scale
import java.io.File
import java.io.FileOutputStream
import kotlin.math.min

object BitmapUtil {

    fun resizeBitmap(
        source: Bitmap,
        maxWidth: Int,
        maxHeight: Int,
        filter: Boolean = true,
    ): Bitmap {
        val width = source.width
        val height = source.height
        if (width <= maxWidth && height <= maxHeight) return source
        val scale = min(maxWidth.toFloat() / width, maxHeight.toFloat() / height)
        val newWidth = (width * scale).toInt()
        val newHeight = (height * scale).toInt()
        return source.scale(newWidth, newHeight, filter)
    }

    fun forceResizeBitmap(
        source: Bitmap,
        width: Int,
        height: Int,
        filter: Boolean = true,
    ): Bitmap = source.scale(width, height, filter)

    fun scaleBitmap(
        source: Bitmap,
        scaleX: Float,
        scaleY: Float,
        filter: Boolean = true,
    ): Bitmap {
        val matrix = Matrix().apply { postScale(scaleX, scaleY) }
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, filter)
    }

    fun bitmapToFile(context: Context, bitmap: Bitmap, fileName: String): File {
        val file = File(context.cacheDir, fileName)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
        }
        return file
    }
}