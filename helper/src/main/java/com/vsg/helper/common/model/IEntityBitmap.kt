package com.vsg.helper.common.model

import android.graphics.Bitmap
import androidx.room.Ignore

interface IEntityBitmap {
    val isBitmap: Boolean

    @Ignore
    fun getPictureShow(): Bitmap?
}