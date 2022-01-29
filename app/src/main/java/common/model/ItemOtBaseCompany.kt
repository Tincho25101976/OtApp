package common.model

import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.vsg.helper.common.adapter.IResultRecyclerAdapter
import com.vsg.helper.common.model.EntityForeignKeyID
import com.vsg.helper.common.model.IEntityPagingLayoutPosition
import com.vsg.helper.common.model.IReference
import com.vsg.helper.common.model.ItemBase
import com.vsg.helper.common.util.addItem.IAddItemEntity
import common.model.master.company.MasterCompany

abstract class ItemOtBaseCompany<T> : ItemOtBase<T>()
        where T : ItemBase,
              T : IResultRecyclerAdapter,
              T : IAddItemEntity,
              T : IReference,
              T : IEntityPagingLayoutPosition {

    //region properties
    @EntityForeignKeyID(10)
    @ColumnInfo(index = true)
    val idCompany: Int = 0

    @EntityForeignKeyID(10)
    @Ignore
    val company: MasterCompany? = null
    //endregion

    //region methods
    //endregion
}