package common.model.master.warehouse

import android.app.Application
import com.vsg.agendaandpublication.common.data.AppDatabase
import com.vsg.helper.common.util.viewModel.IViewModelHasItemsRelationType
import com.vsg.helper.common.util.viewModel.IViewModelUpdateIsDefault
import common.model.master.filter.TypeFilterHasWarehouseItems
import common.model.master.section.SectionViewModel
import common.model.init.viewModel.ViewModelGenericForCode

@ExperimentalStdlibApi
class WarehouseViewModel(context: Application) :
    ViewModelGenericForCode<WarehouseDao, MasterWarehouse>(
        AppDatabase.getInstance(context)?.warehouseDao()!!, context
    ),
    IViewModelHasItemsRelationType<TypeFilterHasWarehouseItems>,
    IViewModelUpdateIsDefault<MasterWarehouse> {


    override fun viewModelViewHasItems(
        idRelation: Int,
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