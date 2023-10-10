package com.ej.defaultcamera_gallary_test

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.ExifInterface
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.IOException

object ImageUtils {

    @RequiresApi(Build.VERSION_CODES.N)
    fun getDrawableRotation(context: Context, drawableResId: Int): Int {
        val resources: Resources = context.resources

        // Drawable 리소스를 비트맵으로 변환
        val drawable: Drawable = resources.getDrawable(drawableResId)
        val bitmap: Bitmap = (drawable as BitmapDrawable).bitmap

        // Exif 메타데이터에서 회전 정보 가져오기
        var rotation = 0
        try {
            val exifInterface = ExifInterface(resources.openRawResourceFd(drawableResId).fileDescriptor)
            val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotation = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> rotation = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> rotation = 270
                else -> rotation = 0
            }
        } catch (e: IOException) {
            Log.e("ImageUtils", "Error reading Exif data: ${e.message}")
        }

        return rotation
    }

//    fun rotateDrawable(drawable: Drawable, degrees: Int): Drawable {
//        if (degrees != 0 && drawable is BitmapDrawable) {
//            val bitmapDrawable = drawable as BitmapDrawable
//            val bitmap: Bitmap = bitmapDrawable.bitmap
//
//            val matrix = Matrix()
//            matrix.setRotate(degrees.toFloat())
//
//            val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
//            return BitmapDrawable(bitmapDrawable.resources, rotatedBitmap)
//        }
//        return drawable
//    }
}