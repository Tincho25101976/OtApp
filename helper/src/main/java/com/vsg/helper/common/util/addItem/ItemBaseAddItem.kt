package com.vsg.helper.common.util.addItem

import android.text.Spanned
import androidx.annotation.DrawableRes
import androidx.room.Ignore
import com.vsg.helper.common.adapter.RecyclerAdapter
import com.vsg.helper.common.model.ItemBase
import com.vsg.helper.helper.string.HelperString.Static.castToHtml
import com.vsg.helper.helper.string.HelperString.Static.toLineSpanned

abstract class ItemBaseAddItem : ItemBase(), IAddItemEntity {
    @Ignore
    override fun descriptionSpanned(): Spanned {
        val sb = StringBuilder()
        if (description.isNotEmpty()) sb.toLineSpanned("Descripción", description)
        return sb.castToHtml()
    }

    @Ignore
    protected abstract fun aTitleRecyclerAdapter(): String

    @DrawableRes
    @Ignore
    protected abstract fun aPictureRecyclerAdapter(): Int

    @Ignore
    override fun getRecyclerAdapter(): RecyclerAdapter =
        RecyclerAdapter(id, aTitleRecyclerAdapter(), descriptionSpanned()).apply {
            rating = -1.0F
            picture = aPictureRecyclerAdapter()
            sizePictureHeight = 32
            sizePictureWidth = 32
            textSizeTitle = 32
        }
}