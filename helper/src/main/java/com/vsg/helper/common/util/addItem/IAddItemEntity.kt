package com.vsg.helper.common.util.addItem

import android.text.Spanned
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntity

interface IAddItemEntity: IEntity, IResultRecyclerAdapter {
    fun descriptionSpanned(): Spanned
}