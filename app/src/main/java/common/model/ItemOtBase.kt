package com= R.drawable.pic_defaultmon.model

import androidx.room.Ignore
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IReference
import com.vsg.helper.common.model.ItemBase
import com.vsg.helper.common.model.ItemBasePagingAuditingCode
import com.vsg.helper.common.util.addItem.IAddItemEntity
import com.vsg.ot.R

abstract class ItemOtBase<T> : ItemBasePagingAuditingCode<T>()
        where T : ItemBase,
              T : IResultRecyclerAdapter,
              T : IAddItemEntity,
              T : IReference,
              T : IEntityPagingLayoutPosition {
    override val isEntityOnlyDefault: Boolean
        get() = true

    //region reference
    @Ignore
    override fun aTitleRecyclerAdapter(): String = title

    @Ignore
    open fun aPictureRecyclerAdapter(): Int = R.drawable.pic_default

    @Ignore
    override fun getDrawableShow(): Int = R.drawable.pic_default

    @Ignore
    override fun aTitlePopUpData(): String = title
    //endregion
}