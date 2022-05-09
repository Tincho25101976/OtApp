package com.vsg.ot.common.model.init.entity

import androidx.room.ColumnInfo
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IEntityPicture
import com.vsg.helper.common.model.IReference
import com.vsg.helper.common.model.ItemBase
import com.vsg.helper.common.util.addItem.IAddItemEntity
import common.model.init.entity.EntityOt

abstract class EntityOtWithPicture<T> : EntityOt<T>(), IEntityPicture
        where T : ItemBase,
              T : IResultRecyclerAdapter,
              T : IAddItemEntity,
              T : IReference,
              T : IEntityPagingLayoutPosition {

    //region properties
    @ColumnInfo(name = "picture", typeAffinity = ColumnInfo.BLOB)
    override var picture: ByteArray? = null
    //endregion

    // recycler adapter
    override val isBitmap: Boolean
        get() = true

    override fun oGetPictureShow(): ByteArray? {
        return this.picture
    }
    //endregion
}