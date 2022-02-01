package com.vsg.agendaandpublication.common.model.itemProduct.company

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import com.vsg.agendaandpublication.common.data.AppDatabase
import com.vsg.agendaandpublication.common.model.itemOperation.operation.operation.OperationViewModel
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.warehouse.WarehouseViewModel
import com.vsg.agendaandpublication.common.model.itemPerson.PersonViewModel
import com.vsg.agendaandpublication.common.model.itemProduct.filter.TypeFilterHasCompanyItems
import com.vsg.agendaandpublication.common.model.itemProduct.product.ProductViewModel
import com.vsg.agendaandpublication.common.model.viewModel.ViewModelGeneric
import com.vsg.utilities.common.util.viewModel.*
import common.model.master.company.CompanyDao
import kotlinx.coroutines.runBlocking

@ExperimentalStdlibApi
class CompanyViewModel(application: Application) :
//MakeGenericViewModelPaging
    ViewModelGeneric<CompanyDao, Company>(
        AppDatabase.getInstance(application)?.companyDao()!!, application
    ),
    IViewModelAllTextSearch,
    IViewModelView<Company>,
    IViewModelAllSimpleList<Company>,
    IViewModelHasItemsRelationType<TypeFilterHasCompanyItems>,
    IViewModelHasItemsRelation {

    fun viewModelGetViewProductWithPicture(id: Long) = runBlocking {
        return@runBlocking Pager(
            pagingConfig,
            0,
            dao.viewCompanyWithProduct(id).asPagingSourceFactory()
        ).flow
    }

    override fun viewModelGetAllTextSearch(): LiveData<List<String>> = runBlocking {
        return@runBlocking dao.viewAllTextSearch()
    }

    override fun viewModelViewAllSimpleList(): List<Company> = dao.viewAllSimpleList() ?: listOf()

    override fun viewModelViewHasItems(
        idRelation: Long,
        filter: TypeFilterHasCompanyItems
    ): Boolean {
        return when (filter) {
            TypeFilterHasCompanyItems.PRODUCT -> ProductViewModel(context).viewModelViewHasItems(
                idRelation
            )
            TypeFilterHasCompanyItems.WAREHOUSE -> WarehouseViewModel(context).viewModelViewHasItems(
                idRelation
            )
            TypeFilterHasCompanyItems.OPERATION -> OperationViewModel(context).viewModelViewHasItems(
                idRelation
            )
            TypeFilterHasCompanyItems.PERSON -> PersonViewModel(context).viewModelViewHasItems(
                idRelation
            )
        }
    }

    override fun viewModelViewHasItems(idRelation: Long): Boolean =
        ProductViewModel(context).viewModelViewHasItems(idRelation)
}