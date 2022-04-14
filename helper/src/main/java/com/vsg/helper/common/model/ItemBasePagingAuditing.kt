package com.vsg.helper.common.model

import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.format.FormatDateString
import com.vsg.helper.common.util.addItem.IAddItemEntity
import com.vsg.helper.helper.date.HelperDate
import com.vsg.helper.helper.date.HelperDate.Companion.toDateString
import com.vsg.helper.ui.adapter.IDataAdapterTitle
import java.util.*

abstract class ItemBasePagingAuditing<T> : ItemBasePagingWithoutPicture<T>(),
    IEntityCreateDate, IDataAdapterTitle
        where T : ItemBase,
              T : IResultRecyclerAdapter,
              T : IAddItemEntity,
              T : IReference,
              T : IEntityPagingLayoutPosition {

    override var createDate: Date = HelperDate.now()
    override val formatDate: FormatDateString
        get() = FormatDateString.AUDIT


    val toCreateDate: String
        get() = createDate.toDateString(FormatDateString.CREATE_DATE)

    override fun equals(other: Any?): Boolean {
        if (other !is ItemBasePagingAuditing<*>) return false
        return super.equals(other)
                && createDate.time == other.createDate.time
    }

    override fun hashCode(): Int = layoutPosition
}