package com.vsg.helper.common.popup

import android.graphics.Bitmap
import android.text.Spanned
import com.vsg.helper.common.model.IIsEnabled

interface IPopUpData : IPopUpParameter, IIsEnabled {
    var title: String
    var body: String
    var toHtml: Spanned
    var icon: Int
    var bitmap: Bitmap?
    val isBitmap: Boolean
    var commandOK: Boolean
    fun isSpanned(): Boolean
}