package com.vsg.agendaandpublication.ui.activities.itemProducto

import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.agendaandpublication.R
import com.vsg.agendaandpublication.common.model.itemProduct.unit.Unit
import com.vsg.agendaandpublication.common.model.itemProduct.unit.UnitDao
import com.vsg.agendaandpublication.common.model.itemProduct.unit.UnitViewModel
import com.vsg.agendaandpublication.ui.activities.itemProducto.util.FilterTypeActivityUnit
import com.vsg.agendaandpublication.ui.common.itemProduct.unit.UICRUDUnit
import com.vsg.utilities.ui.util.CurrentBaseActivityPagingGeneric

@ExperimentalStdlibApi
class UnitActivity :
    CurrentBaseActivityPagingGeneric<UnitActivity, UnitViewModel, UnitDao, Unit, FilterTypeActivityUnit, UICRUDUnit<UnitActivity>>(
        UnitViewModel::class.java,
        FilterTypeActivityUnit::class.java
    ) {
//    override fun oSetSwipeMenuItems(): Int = R.layout.swipe_menu_company_item
    override fun oSetStringTitleForActionBar(): Int = R.string.ActivityItemProductUnitText
    override fun aFinishExecute() {
        onEventMakeFilter = { item, find, it ->
            val filter: PagingData<Unit> =
                when (item) {
                    FilterTypeActivityUnit.NAME -> it.filter { s ->
                        s.name.contains(
                            find,
                            true
                        )
                    }
                    else -> it
                }
            filter
        }
        onEventSetCRUDForApply = { context, operation -> UICRUDUnit(context, operation) }
    }
}