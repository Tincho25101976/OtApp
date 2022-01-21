package com.vsg.helper.common.model

import android.graphics.Bitmap
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.util.addItem.IAddItemEntity

abstract class ItemBasePagingWithoutPicture<T> : ItemBasePaging<T>() where T : ItemBase,
                                                                           T : IResultRecyclerAdapter,
                                                                           T : IAddItemEntity,
                                                                           T : IReference,
                                                                           T : IEntityPagingLayoutPosition {
    override val isBitmap: Boolean
        get() = false

    override fun getPictureShow(): Bitmap? = null
    override fun aBitmapRecyclerAdapter(): Bitmap? = null
}