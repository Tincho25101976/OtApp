package com.vsg.helper.common.model

import android.R
import android.graphics.Bitmap
import android.text.Spanned
import androidx.room.Ignore
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.adapter.RecyclerAdapter
import com.vsg.helper.common.model.util.DrawableShow
import com.vsg.helper.common.util.addItem.IAddItemEntity
import com.vsg.helper.helper.string.HelperString.Static.castToHtml
import com.vsg.helper.helper.string.HelperString.Static.toLineSpanned
import com.vsg.helper.ui.adapter.IDataAdapterTitle

abstract class ItemBasePaging<T> : ItemBase(), IResultRecyclerAdapter, IAddItemEntity, IReference,
    IEntityPagingLayoutPosition, IEntityDrawable, Comparable<T>, IEntityHasItem, IDataAdapterTitle
        where T : ItemBase,
              T : IResultRecyclerAdapter,
              T : IAddItemEntity,
              T : IReference,
              T : IEntityPagingLayoutPosition {

    override val title: String  get() = ""

    @Ignore
    override var layoutPosition: Int = 0

    //region RecyclerAdapter
    @Ignore
    override fun descriptionSpanned(): Spanned {
        val sb = StringBuilder()
        if (description.isNotEmpty()) sb.toLineSpanned("Descripción", description)
        return sb.castToHtml()
    }

    @Ignore
    override var hasItems: Boolean = false

    @Ignore
    protected abstract fun aTitleRecyclerAdapter(): String

    @Ignore
    protected open fun aDrawableRecyclerAdapter(): DrawableShow = getDrawableShow()

    @Ignore
    protected abstract fun aBitmapRecyclerAdapter(): Bitmap?

    @Ignore
    override fun getRecyclerAdapter(): RecyclerAdapter =
        RecyclerAdapter(id, aTitleRecyclerAdapter(), descriptionSpanned()).apply {
            rating = -1.0F
            picture = aDrawableRecyclerAdapter().drawable
            bitmap = aBitmapRecyclerAdapter()
            sizePictureHeight = 32
            sizePictureWidth = 32
            textSizeTitle = 32
        }

    //endregion

    protected abstract fun aEquals(other: Any?): Boolean
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ItemBase) return false
        val basic =
            isEnabled == other.isEnabled
                    && isDefault == isDefault
                    && description == description
                    && id == other.id
        if (!basic) return false
        return aEquals(other)
    }

    override fun compareTo(other: T): Int = when (this == other) {
        true -> 0
        false -> 1
    }

    override fun hashCode(): Int = layoutPosition
    //endregion
}