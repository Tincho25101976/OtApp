package com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.warehouse

import android.app.Application
import com.vsg.agendaandpublication.common.data.AppDatabase
import com.vsg.agendaandpublication.common.model.itemOperation.filter.TypeFilterHasWarehouseItems
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.section.SectionViewModel
import com.vsg.utilities.common.util.viewModel.IViewModelHasItemsRelationType
import com.vsg.utilities.common.util.viewModel.IViewModelUpdateIsDefault
import common.model.master.warehouse.MasterWarehouse
import common.model.master.warehouse.WarehouseDao

@ExperimentalStdlibApi
class WarehouseViewModel(context: Application) :
    ViewModelGenericForCode<WarehouseDao, MasterWarehouse>(
        AppDatabase.getInstance(context)?.warehouseDao()!!, context
    ),
    IViewModelHasItemsRelationType<TypeFilterHasWarehouseItems>,
    IViewModelUpdateIsDefault<MasterWarehouse>{
    override fun viewModelInsert(item: MasterWarehouse): Boolean {
        val code = viewModelEncode(item) ?: return false
        return super.viewModelInsert(code)
    }

    override fun viewModelEncode(item: MasterWarehouse): MasterWarehouse? {
        item.number = viewModelNextAutoCode(item.idCompany)
        item.valueCode = item.code
        if (item.valueCode.isEmpty()) return null
        return item
    }

    override fun viewModelViewHasItems(
        idRelation: Long,
        filter: TypeFilterHasWarehouseItems
    ): Boolean {
        return when (filter) {
            TypeFilterHasWarehouseItems.SECTION -> SectionViewModel(context).viewModelViewHasItems(
                idRelation
            )
        }
    }

    override fun viewModelSetIsDefault(item: MasterWarehouse) =
        dao.updateSetAllIsDefault(item.id, item.idCompany)
}