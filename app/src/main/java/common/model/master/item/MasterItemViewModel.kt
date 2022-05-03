package common.model.master.item

import android.app.Application
import com.vsg.ot.common.data.AppDatabase
import com.vsg.helper.common.util.viewModel.IViewModelHasItemsRelationType
import common.model.init.viewModel.ViewModelGenericOt
import common.model.master.batch.MasterBatchViewModel
import common.model.master.filter.TypeFilterHasProductItems

@ExperimentalStdlibApi
class MasterItemViewModel(context: Application) :
    ViewModelGenericOt<MasterItemDao, MasterItem>(
        AppDatabase.getInstance(context)?.itemDao()!!,
        context
    ),
    IViewModelHasItemsRelationType<TypeFilterHasProductItems> {

    //region search
    override fun viewModelViewHasItems(
        idRelation: Int,
        filter: TypeFilterHasProductItems
    ): Boolean {
        return when (filter) {
            TypeFilterHasProductItems.BATCH -> MasterBatchViewModel(context).viewModelViewHasItems(
                idRelation
            )
        }
    }
    //endregion
}