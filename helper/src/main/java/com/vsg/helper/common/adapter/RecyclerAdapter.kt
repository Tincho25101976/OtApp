package com.vsg.helper.common.adapter

import android.graphics.Bitmap
import android.text.Spanned
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange

class RecyclerAdapter(
    override var id: Int,
    override var title: String,
    override var body: Spanned
) :
    IRecyclerAdapter {

    @IntRange(from = 24, to = 128)
    override var sizePictureHeight: Int = IRecyclerAdapter.SIZE_PICTURE_HEIGHT_DEFAULT
        get() = when (field <= 0) {
            true -> IRecyclerAdapter.SIZE_PICTURE_HEIGHT_DEFAULT
            false -> field
        }

    @IntRange(from = 24, to = 128)
    override var sizePictureWidth: Int = IRecyclerAdapter.SIZE_PICTURE_WIDTH_DEFAULT
        get() = when (field <= 0) {
            true -> IRecyclerAdapter.SIZE_PICTURE_WIDTH_DEFAULT
            false -> field
        }

    @IntRange(from = 12, to = 48)
    override var textSizeTitle: Int = IRecyclerAdapter.SIZE_TEXT_TITLE
        get() = when (field <= 0) {
            true -> IRecyclerAdapter.SIZE_TEXT_TITLE
            false -> field
        }

    @IntRange(from = 12, to = 48)
    override var textSizeBody: Int = IRecyclerAdapter.SIZE_TEXT_BODY
        get() = when (field <= 0) {
            true -> IRecyclerAdapter.SIZE_TEXT_BODY
            false -> field
        }

    @DrawableRes
    override var picture: Int = 0
    override var bitmap: Bitmap? = null
    override var isEnabled: Boolean = true
    override var isBitmap: Boolean = false
        get() = bitmap != null && bitmap!!.byteCount > 0
    override var rating: Float = 0.0F
}