package com.vsg.ot.ui.activities.master.util

import com.vsg.helper.common.model.IEntity
import com.vsg.helper.ui.adapter.IDataAdapterTitle

class FilterSearchCompany(val type: FilterTypeActivityCompany, override var id: Int) :
    IEntity, IDataAdapterTitle {
    override val title: String
        get() = type.title
}