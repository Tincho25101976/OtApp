package com.vsg.helper.common.model

import androidx.annotation.DrawableRes
import androidx.room.Ignore

interface IEntityDrawable {
    @Ignore
    @DrawableRes
    fun getDrawableShow(): Int
}