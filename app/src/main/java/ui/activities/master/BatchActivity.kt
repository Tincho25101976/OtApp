package com.vsg.agendaandpublication.ui.activities.itemOperation.batch

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.agendaandpublication.R
import com.vsg.agendaandpublication.common.model.itemOperation.batch.Batch
import com.vsg.agendaandpublication.common.model.itemOperation.batch.BatchDao
import com.vsg.agendaandpublication.common.model.itemOperation.batch.BatchViewModel
import com.vsg.agendaandpublication.common.model.itemOperation.filter.TypeFilterHasProductItems
import com.vsg.agendaandpublication.common.model.itemProduct.company.Company
import com.vsg.agendaandpublication.common.model.itemProduct.company.CompanyViewModel
import com.vsg.agendaandpublication.common.model.itemProduct.product.Product
import com.vsg.agendaandpublication.common.model.itemProduct.product.ProductDao
import com.vsg.agendaandpublication.common.model.itemProduct.product.ProductViewModel
import com.vsg.agendaandpublication.ui.activities.itemOperation.util.FilterTypeActivityBatch
import com.vsg.agendaandpublication.ui.activities.itemProducto.util.FilterTypeActivityProduct
import com.vsg.agendaandpublication.ui.common.itemOperation.batch.UICRUDBatch
import com.vsg.utilities.ui.util.CurrentBaseActivityPagingGenericRelationParentWithRelation
import kotlinx.coroutines.flow.Flow

@ExperimentalStdlibApi
class BatchActivity :
    CurrentBaseActivityPagingGenericRelationParentWithRelation<BatchActivity, BatchViewModel, BatchDao, Batch, FilterTypeActivityBatch, UICRUDBatch<BatchActivity>,
            FilterTypeActivityProduct, ProductViewModel, ProductDao, Product, TypeFilterHasProductItems,
            Company>(
        BatchViewModel::class.java, ProductViewModel::class.java,
        FilterTypeActivityBatch::class.java, FilterTypeActivityProduct::class.java
    ) {
    override var factorHeightForCustomViewer: Double = 0.75
    override fun oSetStringTitleForActionBar(): Int = R.string.ActivityItemOperationBatchText
    override fun aSetActivity(): BatchActivity = this
    override fun aFinishExecutePagingGenericRelationParent() {
        onEventMakeFilter = { item, find, it ->
            val filter: PagingData<Batch> =
                when (item) {
                    FilterTypeActivityBatch.PRODUCT -> it.filter { s ->
                        if (s.product == null) s.product =
                            currentViewModel().viewModelGetProduct(s.idProduct)
                        var data = false
                        if (s.product != null) {
                            data = s.product?.name!!.contains(
                                find,
                                true
                            )
                        }
                        data
                    }
                    FilterTypeActivityBatch.VALUE_CODE -> it.filter { s ->
                        s.valueCode.contains(
                            find,
                            true
                        )
                    }
                    else -> it
                }
            filter
        }
        onEventMakeFilterResult = { item, find, it ->
            val filter: PagingData<Product> =
                when (item) {
                    FilterTypeActivityProduct.NAME -> it.filter { s -> s.name.contains(find, true) }
                    FilterTypeActivityProduct.CODE -> it.filter { s -> s.code.contains(find, true) }
                    FilterTypeActivityProduct.PROVIDER_CODE -> it.filter { s ->
                        s.providerCode.contains(
                            find,
                            true
                        )
                    }
                    else -> it
                }
            filter
        }
        onEventSetCRUDForApply = { context, operation ->
            UICRUDBatch(
                context,
                operation,
                parent!!
            ).apply { factorHeight = 0.8 }
        }
        onEventSwipeGetViewForMenu = {
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
        }
        onEventGetListTextSearch = {
            val data = when (parent == null) {
                true -> MutableLiveData()
                false -> currentViewModel().viewModelGetAllTextSearch(parent?.id!!)
            }
            data
        }
        onEventGetViewAllPaging = {
            val data: Flow<PagingData<Batch>>? = when (parent == null) {
                true -> null
                false -> currentViewModel().viewModelGetViewAllPaging(parent?.id!!)
            }
            data
        }
        onEventSetParentFilterHasItems = { TypeFilterHasProductItems.BATCH }
    }
    override fun oHintForParent(): String = this.getString(R.string.HintParentForProduct)

    override fun aRelation(): Company? = getExtraParameter(CompanyViewModel::class.java)
    override fun aCurrentListOfParent(): List<Product>? {
        relation = aRelation()
        return when (relation == null) {
            true -> null
            false -> makeViewModel(ProductViewModel::class.java)
                .viewModelViewAllSimpleList(relation!!.id)
        }
    }
}