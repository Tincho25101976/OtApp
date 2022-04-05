package com.vsg.agendaandpublication.ui.activities.itemProducto

import android.widget.RelativeLayout
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.agendaandpublication.R
import com.vsg.agendaandpublication.common.model.itemProduct.company.Company
import com.vsg.agendaandpublication.common.model.itemProduct.company.CompanyDao
import com.vsg.agendaandpublication.common.model.itemProduct.company.CompanyViewModel
import com.vsg.agendaandpublication.common.model.itemProduct.filter.TypeFilterHasCompanyItems
import com.vsg.agendaandpublication.common.model.itemProduct.product.Product
import com.vsg.agendaandpublication.common.model.itemProduct.product.ProductDao
import com.vsg.agendaandpublication.common.model.itemProduct.product.ProductViewModel
import com.vsg.agendaandpublication.ui.activities.itemOperation.batch.BatchActivity
import com.vsg.agendaandpublication.ui.activities.itemProducto.util.FilterTypeActivityCompany
import com.vsg.agendaandpublication.ui.activities.itemProducto.util.FilterTypeActivityProduct
import com.vsg.agendaandpublication.ui.common.itemProduct.product.UICRUDProduct
import com.vsg.utilities.ui.util.CurrentBaseActivityPagingGenericRelationParent
import kotlinx.coroutines.flow.Flow

@ExperimentalStdlibApi
class ProductActivity :
    CurrentBaseActivityPagingGenericRelationParent
        <ProductActivity, ProductViewModel, ProductDao, Product, FilterTypeActivityProduct, UICRUDProduct<ProductActivity>,
                FilterTypeActivityCompany, CompanyViewModel, CompanyDao, Company, TypeFilterHasCompanyItems>
        (
        ProductViewModel::class.java,
        CompanyViewModel::class.java,
        FilterTypeActivityProduct::class.java,
        FilterTypeActivityCompany::class.java
    ) {

    override fun oSetStringTitleForActionBar(): Int = R.string.ActivityItemProductProductoText
    override fun aSetActivity(): ProductActivity = this
    override fun oSetSwipeMenuItems(): Int = R.layout.swipe_menu_product_item
    override fun aFinishExecutePagingGenericRelationParent() {
        onEventMakeFilter = { item, find, it ->
            val filter: PagingData<Product> =
                when (item) {
                    FilterTypeActivityProduct.NAME -> it.filter { s -> s.name.contains(find, true) }
                    FilterTypeActivityProduct.CODE -> it.filter { s -> s.code.contains(find, true) }
                    FilterTypeActivityProduct.PROVIDER_CODE -> it.filter { s ->
                        s.providerCode.contains(find, true)
                    }
                    else -> it
                }
            filter
        }
        onEventMakeFilterResult = { item, find, it ->
            val filter: PagingData<Company> =
                when (item) {
                    FilterTypeActivityCompany.NAME -> it.filter { s -> s.name.contains(find, true) }
                    else -> it
                }
            filter
        }
        onEventSetCRUDForApply = { context, operation ->
            UICRUDProduct(
                context,
                operation,
                currentViewModel(),
                parent!!
            ).apply { factorHeight = 0.8 }
        }
        onEventSwipeGetViewForMenu = {
            it.findViewById<RelativeLayout>(R.id.SwipeMenuProductItemPrice)
                .setOnClickListener {
                    if (getItem() != null) loadActivity(
                        PriceActivity::class.java, getItem()!!
                    )
                }
            it.findViewById<RelativeLayout>(R.id.SwipeMenuProductItemMail)
                .setOnClickListener {
                    if (getItem() != null) loadActivity(
                        PictureActivity::class.java, getItem()!!
                    )
                }
            it.findViewById<RelativeLayout>(R.id.SwipeMenuProductItemBatch)
                .setOnClickListener {
                    if (getItem() != null) loadActivity(
                        BatchActivity::class.java, getItem()!!, extra = parent
                    )
                }
        }
        onEventGetListTextSearch = {
            val data = when (parent == null) {
                true -> MutableLiveData()
                false -> currentViewModel().viewModelGetAllTextSearch(parent?.id!!)
            }
            data
        }
        onEventGetViewAllPaging = {
            val data: Flow<PagingData<Product>>? = when (parent == null) {
                true -> null
                false -> currentViewModel().viewModelGetViewAllPaging(parent?.id!!)
            }
            data
        }
        onEventSetParentFilterHasItems = { TypeFilterHasCompanyItems.PRODUCT }
    }
    override fun aCurrentListOfParent(): List<Company> = makeViewModel(CompanyViewModel::class.java)
        .viewModelViewAllSimpleList()




//    init {
//        onEventGetIdRelationFromIntent = {
//            company = getParent(CompanyViewModel::class.java)
//            if (company == null) {
//                val items =
//                    this.makeViewModel(CompanyViewModel::class.java).viewModelViewAllSimpleList()
//                if (items.count() > 1) {
//                    UIAlertDialogResultCompany(
//                        this,
//                        this.makeViewModel(CompanyViewModel::class.java)
//                    ).apply {
//                        factorHeight = 0.45
//                        onEventClickOK = { setParent(it) }
//                        onEventGetItemAfterBind = { t ->
//                            t.hasItems = currentViewModel().viewModelViewHasItems(t.id)
//                            t
//                        }
//                        make()
//                    }
//                }
//                if (items.count() == 1) setParent(items.first())
//            }
//        }
//    }






//    override fun aFinishExecute() {
//        onEventMakeFilter = { item, find, it ->
//            val filter: PagingData<Product> =
//                when (item) {
//                    FilterTypeActivityProduct.NAME -> it.filter { s -> s.name.contains(find, true) }
//                    FilterTypeActivityProduct.CODE -> it.filter { s -> s.code.contains(find, true) }
//                    FilterTypeActivityProduct.PROVIDER_CODE -> it.filter { s ->
//                        s.providerCode.contains(find, true)
//                    }
//                    else -> it
//                }
//            filter
//        }
//        onEventSetCRUDForApply = { context, operation ->
//            UICRUDProduct(
//                context,
//                operation,
//                currentViewModel(),
//                company!!
//            ).apply { factorHeight = 0.8 }
//        }
//        onEventSwipeGetViewForMenu = {
//            it.findViewById<RelativeLayout>(R.id.SwipeMenuProductItemPrice)
//                .setOnClickListener {
//                    if (getItem() != null) loadActivity(
//                        PriceActivity::class.java, getItem()!!
//                    )
//                }
//            it.findViewById<RelativeLayout>(R.id.SwipeMenuProductItemMail)
//                .setOnClickListener {
//                    if (getItem() != null) loadActivity(
//                        PictureActivity::class.java, getItem()!!
//                    )
//                }
//            it.findViewById<RelativeLayout>(R.id.SwipeMenuProductItemBatch)
//                .setOnClickListener {
//                    if (getItem() != null) loadActivity(
//                        BatchActivity::class.java, getItem()!!, extra = company
//                    )
//                }
//        }
//        onEventGetListTextSearch = {
//            val data = when (company == null) {
//                true -> MutableLiveData()
//                false -> currentViewModel().viewModelGetAllTextSearch(company?.id!!)
//            }
//            data
//        }
//        onEventGetViewAllPaging = {
//            val data: Flow<PagingData<Product>>? = when (company == null) {
//                true -> null
//                false -> currentViewModel().viewModelGetViewAllPaging(company?.id!!)
//            }
//            data
//        }
//        setParent(this.company)
//    }
//    private fun setParent(it: Company?) {
//        if (it == null) return
//        company = it
//        if (company != null) {
//            fillTextSearch()
//            fillAdapter()
//        }
//    }
}