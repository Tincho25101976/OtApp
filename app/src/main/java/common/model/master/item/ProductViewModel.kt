package common.model.master.item

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import com.vsg.agendaandpublication.common.data.AppDatabase
import com.vsg.helper.common.util.viewModel.*
import common.model.master.batch.BatchViewModel
import common.model.master.filter.TypeFilterHasProductItems
import common.model.init.viewModel.ViewModelGenericForCode
import kotlinx.coroutines.runBlocking

@ExperimentalStdlibApi
class ProductViewModel(context: Application) :
    ViewModelGenericForCode<ProductDao, MasterItem>(
        AppDatabase.getInstance(context)?.productDao()!!, context
    ),
    IViewModelAllTextSearch,
    IViewModelAllSimpleListIdRelation<MasterItem>,
    IViewModelHasItemsRelation,
    IViewModelAllPaging<MasterItem>,
    IViewModelHasItemsRelationType<TypeFilterHasProductItems> {

    //region list
    override fun viewModelGetAllTextSearch(): LiveData<List<String>> = MutableLiveData()
    override fun viewModelGetViewAllPaging() = runBlocking {
        return@runBlocking Pager(
            pagingConfig,
            0,
            dao.viewAllPaging().asPagingSourceFactory()
        ).flow
    }

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