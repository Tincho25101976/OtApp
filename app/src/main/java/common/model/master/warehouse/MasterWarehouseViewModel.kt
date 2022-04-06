package common.model.master.warehouse

import android.app.Application
import com.vsg.agendaandpublication.common.data.AppDatabase
import com.vsg.helper.common.util.viewModel.IViewModelHasItemsRelationType
import com.vsg.helper.common.util.viewModel.IViewModelUpdateIsDefault
import common.model.init.viewModel.ViewModelGenericOt
import common.model.master.filter.TypeFilterHasWarehouseItems
import common.model.master.section.MasterSectionViewModel

@ExperimentalStdlibApi
class MasterWarehouseViewModel(context: Application) :
    ViewModelGenericOt<MasterWarehouseDao, MasterWarehouse>(
        AppDatabase.getInstance(context)?.warehouseDao()!!, context
    ),
    IViewModelHasItemsRelationType<TypeFilterHasWarehouseItems>,
    IViewModelUpdateIsDefault<MasterWarehouse> {


    override fun viewModelViewHasItems(
        idRelation: Int,
        filter: TypeFilterHasWarehouseItems
    ): Boolean {
        return when (filter) {
            TypeFilterHasWarehouseItems.SECTION -> MasterSectionViewModel(context).viewModelViewHasItems(
                idRelation
            )
        }
    }

    override fun viewModelSetIsDefault(item: MasterWarehouse) =
        dao.updateSetAllIsDefault(item.id, item.idCompany)
}