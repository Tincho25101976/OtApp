package com.vsg.agendaandpublication.common.model.itemProduct.product

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import com.vsg.agendaandpublication.common.data.AppDatabase
import com.vsg.agendaandpublication.common.model.itemOperation.batch.BatchViewModel
import com.vsg.agendaandpublication.common.model.itemOperation.filter.TypeFilterHasProductItems
import com.vsg.agendaandpublication.common.model.itemOperation.setting.SectionDefaultViewModel
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.section.SectionViewModel
import com.vsg.agendaandpublication.common.model.itemPerson.Person
import com.vsg.agendaandpublication.common.model.itemProduct.category.Category
import com.vsg.agendaandpublication.common.model.itemProduct.category.CategoryViewModel
import com.vsg.agendaandpublication.common.model.itemProduct.price.Price
import com.vsg.agendaandpublication.common.model.itemProduct.price.PriceViewModel
import com.vsg.agendaandpublication.common.model.itemProduct.relationship.ProductContext
import com.vsg.agendaandpublication.common.model.itemProduct.unit.UnitViewModel
import com.vsg.agendaandpublication.common.model.viewModel.ViewModelGenericForCode
import com.vsg.utilities.common.util.viewModel.*
import com.vsg.utilities.helper.Helper.Companion.toPadStart
import common.model.master.item.ProductDao
import kotlinx.coroutines.runBlocking

@ExperimentalStdlibApi
class ProductViewModel(context: Application) :
    ViewModelGenericForCode<ProductDao, Product>(
        AppDatabase.getInstance(context)?.productDao()!!, context
    ), IViewModelAllTextSearch, IViewModelAllSimpleListIdRelation<Product>,
    IViewModelHasItemsRelation, IViewModelAllPaging<Product>,
    IViewModelHasItemsRelationType<TypeFilterHasProductItems> {

    //region embedded
    fun viewModelGetViewProductWithPicture(id: Long) = runBlocking {
        return@runBlocking Pager(
            pagingConfig,
            0,
            dao.viewProductWithPicture(id).asPagingSourceFactory()
        ).flow
    }

    fun viewModelGetViewProductContext(id: Long): ProductContext? = dao.viewProductContext(id)
//    fun viewModelGetViewProductUnit(id: Long): ProductUnit? = dao.viewProductUnit(id)
    //endregion

    //region entities
    fun viewModelGetUnits(): List<Unit> = UnitViewModel(context).viewModelViewAllSimpleList()
    fun viewModelGetCategories(): List<Category> =
        CategoryViewModel(context).viewModelViewAllSimpleList()
    //endregion

    //region list
    override fun viewModelGetAllTextSearch(): LiveData<List<String>> = MutableLiveData()
    override fun viewModelGetViewAllPaging() = runBlocking {
        return@runBlocking Pager(
            pagingConfig,
            0,
            dao.viewAllPaging().asPagingSourceFactory()
        ).flow
    }

    fun viewModelViewAllSimpleListWithLastPrice(idRelation: Long): List<Product> {
        val data = super.viewModelViewAllSimpleList(idRelation)
        if (!data.any()) return data
        PriceViewModel(context).apply {
            data.forEach {
                var price: Price? = null
                try {
                    price = this.viewModelGetCurrentPrice(it)
                } catch (ex: Exception) {
                }
                if (price != null) it.precies.add(price)
            }
        }
        val defaultSection =
            SectionDefaultViewModel(context).viewModelGetDefaultSectionByCompany(idRelation)
        SectionViewModel(context).apply {
            data.forEach {
                it.section = when (it.idSection <= 0) {
                    true -> defaultSection
                    false -> this.viewModelView(it.idSection)
                }
            }
        }
        return data
    }
    //endregion

    //region add
    override fun viewModelInsert(item: Product): Boolean {
        val code = viewModelEncode(item) ?: return false
        return super.viewModelInsert(code)
    }

    override fun viewModelEncode(item: Product): Product? {
        item.number = viewModelNextAutoCode(item.idCompany)
        if (item.number <= 0) onRaiseException("El valor debe ser un numero positivo", 1)
        item.code = item.number.toPadStart(item.lenCode)
        if (item.code.isEmpty()) return null
        item.valueCode = item.code
        return item
    }
    //endregion

    //region search
    override fun viewModelViewHasItems(
        idRelation: Long,
        filter: TypeFilterHasProductItems
    ): Boolean {
        return when (filter) {
            TypeFilterHasProductItems.BATCH -> BatchViewModel(context).viewModelViewHasItems(
                idRelation
            )
        }
    }
    //endregion

    //region util
    fun viewModelGetPrefix(id: Long): String = dao.viewPrefix(id)
    //endregion

    //region getBy
    fun viewModelViewGetByPerson(person: Person): List<Product> =
        dao.viewAllGetByPerson(person.id) ?: listOf()

    fun viewModelViewGetByCategory(category: Category): List<Product> =
        dao.viewAllGetByCategory(category.id) ?: listOf()
    //endregion
}