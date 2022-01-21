package com.vsg.helper.common.model

import android.text.Spanned

interface IReference: IEntityDrawable, IEntityBitmap {
    fun reference(): Spanned

}