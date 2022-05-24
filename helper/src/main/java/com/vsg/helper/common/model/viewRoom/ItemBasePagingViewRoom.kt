package com.vsg.helper.common.model.viewRoom

import android.graphics.Bitmap
import android.text.Spanned
import com.vsg.helper.common.adapter.RecyclerAdapter
import com.vsg.helper.common.model.IEntityDrawable
import com.vsg.helper.common.model.IEntityHasItem
import com.vsg.helper.common.model.IReference
import com.vsg.helper.common.model.ItemBase
import com.vsg.helper.common.model.util.DrawableShow
import com.vsg.helper.helper.string.HelperString.Static.castToHtml
import com.vsg.helper.ui.adapter.IDataAdapterTitle

abstract class ItemBasePagingViewRoom<T> : ItemBase(),
    IEntityViewRoom<T>,
    IReference,
    IEntityDrawable,
    IEntityHasItem,
    IDataAdapterTitle
        where T : ItemBase {

    override var layoutPosition: Int = 0
    override val isBitmap: Boolean get() = false


    //region RecyclerAdapter
    private fun descriptionSpanned(): Spanned {
        val sb = StringBuilder()
        if (description.isNotEmpty()) sb.append(description)
        return sb.castToHtml()
    }

    override var hasItems: Boolean = false
    protected open fun aTitleRecyclerAdapter(): String = description
    override fun aTitlePopUpData(): String = description
    override fun descriptionView(): Spanned = description.castToHtml()
    override fun getPictureShow(): Bitmap? = null

    protected open fun aDrawableRecyclerAdapter(): DrawableShow = getDrawableShow()

    override fun getRecyclerAdapter(): RecyclerAdapter =
        RecyclerAdapter(id, aTitleRecyclerAdapter(), reference()).apply {
            rating = -1.0F
            picture = aDrawableRecyclerAdapter().drawable
            bitmap = null
            isEnabled = this@ItemBasePagingViewRoom.isEnabled
            sizePictureHeight = 32
            sizePictureWidth = 32
            textSizeTitle = 32
        }

    //endregion

    //region compareTo
    protected abstract fun aEquals(other: Any?): Boolean
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ItemBase) return false
        val basic =
            isEnabled == other.isEnabled && isDefault == isDefault && description == description
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