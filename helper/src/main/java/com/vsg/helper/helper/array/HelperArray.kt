package com.vsg.helper.helper.array

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlin.random.Random

class HelperArray {
    companion object {
        fun ByteArray?.toBitmap(): Bitmap? {
            if (this == null || this.isEmpty()) return null
            return BitmapFactory.decodeByteArray(this, 0, this.size)
        }

        //region list
        fun <T> List<T>?.toRandom(): T? {
            if (this == null || !this.any()) return null
            return when (this.count() == 1) {
                true -> this[0]
                else -> this[Random.nextInt(0, this.count() - 1)]
            }
        }
        //endregion
    }
}