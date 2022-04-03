package common.model.master.company

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import com.vsg.agendaandpublication.common.data.AppDatabase
import com.vsg.helper.common.model.viewModel.ViewModelGeneric
import com.vsg.helper.common.util.viewModel.*
import common.model.init.viewModel.ViewModelStoredMap
import common.model.master.filter.TypeFilterHasCompanyItems
import common.model.master.item.MasterItemViewModel
import common.model.master.warehouse.WarehouseViewModel
import kotlinx.coroutines.runBlocking

@ExperimentalStdlibApi
class MasterCompanyViewModel(application: Application) :
    ViewModelGeneric<MasterCompanyDao, MasterCompany>(
        AppDatabase.getInstance(application)?.companyDao()!!,
        application,
        ViewModelStoredMap()
    ),
    IViewModelAllTextSearch,
    IViewModelView<MasterCompany>,
    IViewModelAllSimpleList<MasterCompany>,
    IViewModelHasItemsRelationType<TypeFilterHasCompanyItems>,
    IViewModelHasItemsRelation {

    fun viewModelGetViewProductWithPicture(id: Int) = runBlocking {
        return@runBlocking Pager(
            pagingConfig,
            0,
            dao.viewCompanyWithProduct(id).asPagingSourceFactory()
        ).flow
    }

    override fun viewModelGetAllTextSearch(): LiveData<List<String>> = runBlocking {
        return@runBlocking dao.viewAllTextSearch()
    }

    override fun viewModelViewAllSimpleList(): List<MasterCompany> =
        dao.viewAllSimpleList() ?: listOf()

    override fun viewModelViewHasItems(
        idRelation: Int,
        filter: TypeFilterHasCompanyItems
    ): Boolean {
        return when (filter) {
            TypeFilterHasCompanyItems.PRODUCT -> MasterItemViewModel(context).viewModelViewHasItems(
                idRelation
            )
            TypeFilterHasCompanyItems.WAREHOUSE -> WarehouseViewModel(context).viewModelViewHasItems(
                idRelation
            )
        }
    }

    override fun viewModelViewHasItems(idRelation: Int): Boolean =
        MasterItemViewModel(context).viewModelViewHasItems(idRelation)
}