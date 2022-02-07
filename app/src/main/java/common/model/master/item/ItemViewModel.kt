package common.model.master.item

import android.app.Application
import com.vsg.agendaandpublication.common.data.AppDatabase
import com.vsg.helper.common.util.viewModel.IViewModelHasItemsRelationType
import common.model.init.viewModel.ViewModelGenericOt
import common.model.master.batch.BatchViewModel
import common.model.master.filter.TypeFilterHasProductItems

@ExperimentalStdlibApi
class ItemViewModel(context: Application) :
    ViewModelGenericOt<ItemDao, MasterItem>(
        AppDatabase.getInstance(context)?.itemDao()!!, context
    ),
    IViewModelHasItemsRelationType<TypeFilterHasProductItems> {

    //region search
    override fun viewModelViewHasItems(
        idRelation: Int,
        filter: TypeFilterHasProductItems
    ): Boolean {
        return when (filter) {
            TypeFilterHasProductItems.BATCH -> BatchViewModel(context).viewModelViewHasItems(
                idRelation
            )
        }
    }
    //endregion
}