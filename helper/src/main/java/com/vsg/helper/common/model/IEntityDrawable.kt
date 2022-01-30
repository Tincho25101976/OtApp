package com.vsg.helper.common.model

import androidx.room.Ignore
import com.vsg.helper.common.model.util.DrawableShow

interface IEntityDrawable {
    @Ignore
    fun getDrawableShow(): DrawableShow
}