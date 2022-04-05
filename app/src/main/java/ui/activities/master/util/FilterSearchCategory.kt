package com.vsg.agendaandpublication.ui.activities.itemProducto.util

import com.vsg.utilities.common.model.IEntity
import com.vsg.utilities.ui.adapter.IDataAdapterTitle

class FilterSearchCategory(val type: FilterTypeActivityCategory, override var id: Long) :
    IEntity, IDataAdapterTitle {
    override val title: String
        get() = type.title
}