package com.vsg.helper.common.adapter

import android.graphics.Bitmap
import android.text.Spanned
import com.vsg.helper.common.model.IIsEnabled

interface IRecyclerAdapter : IIsEnabled {
    var title: String
    var body: Spanned
    var picture: Int
    var bitmap: Bitmap?
    var isBitmap: Boolean
    var rating: Float

    var sizePictureHeight: Int
    var sizePictureWidth: Int
    var id: Int

    var textSizeTitle: Int
    var textSizeBody: Int

    companion object Static {
        const val SIZE_PICTURE_HEIGHT_DEFAULT: Int = 48
        const val SIZE_PICTURE_WIDTH_DEFAULT: Int = 48
        const val SIZE_TEXT_BODY: Int = 14
        const val SIZE_TEXT_TITLE: Int = 14
    }
}