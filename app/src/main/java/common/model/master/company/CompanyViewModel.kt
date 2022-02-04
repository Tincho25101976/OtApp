package common.model.master.company

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import com.vsg.agendaandpublication.common.data.AppDatabase
import common.model.master.warehouse.WarehouseViewModel
import common.model.master.filter.TypeFilterHasCompanyItems
import common.model.master.item.ProductViewModel
import com.vsg.helper.common.util.viewModel.*
import common.model.init.viewModel.ViewModelGenericForCode
import kotlinx.coroutines.runBlocking

@ExperimentalStdlibApi
class CompanyViewModel(application: Application) :
//MakeGenericViewModelPaging
    ViewModelGenericForCode<CompanyDao, MasterCompany>(
        AppDatabase.getInstance(application)?.companyDao()!!, application
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
            TypeFilterHasCompanyItems.PRODUCT -> ProductViewModel(context).viewModelViewHasItems(
                idRelation
            )
            TypeFilterHasCompanyItems.WAREHOUSE -> WarehouseViewModel(context).viewModelViewHasItems(
                idRelation
            )
        }
    }

    override fun viewModelViewHasItems(idRelation: Int): Boolean =
        ProductViewModel(context).viewModelViewHasItems(idRelation)
}