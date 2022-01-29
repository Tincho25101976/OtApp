package com.vsg.helper.common.model

import android.graphics.Bitmap
import androidx.room.Ignore
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.util.addItem.IAddItemEntity
import com.vsg.helper.helper.array.HelperArray.Companion.toBitmap
import com.vsg.helper.helper.bitmap.HelperBitmap.Companion.grayScale

abstract class ItemBasePagingWithoutPicture<T> : ItemBasePaging<T>() where T : ItemBase,
                                                                           T : IResultRecyclerAdapter,
                                                                           T : IAddItemEntity,
                                                                           T : IReference,
                                                                           T : IEntityPagingLayoutPosition {
    //override val isBitmap: Boolean
    //    get() = false
    override val isBitmap: Boolean
        get() {
            val picture = oGetPictureShow()
            val value = when (picture == null || picture.isEmpty()) {
                true -> false
                false -> true
            }
            return value
        }

    @Ignore
    open fun oGetPictureShow(): ByteArray? = null
    override fun getPictureShow(): Bitmap? {
        val picture = oGetPictureShow()
        val data = when (picture == null || picture.isEmpty()) {
            true -> null
            false -> {
                val bitmap = picture.toBitmap()
                when (isEnabled) {
                    true -> bitmap
                    false -> bitmap?.grayScale()
                }
            }
        }
        return data
    }

    //override fun getPictureShow(): Bitmap? = null
    override fun aBitmapRecyclerAdapter(): Bitmap? = getPictureShow()
}