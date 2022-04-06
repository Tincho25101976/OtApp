package com.vsg.ot.ui.activities.master

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.helper.ui.util.CurrentBaseActivityPagingGenericRelationParentWithRelation
import com.vsg.ot.R
import com.vsg.ot.ui.activities.master.util.FilterTypeActivityBatch
import com.vsg.ot.ui.activities.master.util.FilterTypeActivityProduct
import com.vsg.ot.ui.common.master.batch.UICRUDBatch
import common.model.master.batch.MasterBatch
import common.model.master.batch.MasterBatchDao
import common.model.master.batch.MasterBatchViewModel
import common.model.master.company.MasterCompany
import common.model.master.company.MasterCompanyViewModel
import common.model.master.filter.TypeFilterHasProductItems
import common.model.master.item.MasterItem
import common.model.master.item.MasterItemDao
import common.model.master.item.MasterItemViewModel
import kotlinx.coroutines.flow.Flow

@ExperimentalStdlibApi
class BatchActivity :
    CurrentBaseActivityPagingGenericRelationParentWithRelation<BatchActivity, MasterBatchViewModel, MasterBatchDao, MasterBatch, FilterTypeActivityBatch, UICRUDBatch<BatchActivity>,
            FilterTypeActivityProduct, MasterItemViewModel, MasterItemDao, MasterItem, TypeFilterHasProductItems,
            MasterCompany>(
        MasterBatchViewModel::class.java,
        MasterItemViewModel::class.java,
        FilterTypeActivityBatch::class.java,
        FilterTypeActivityProduct::class.java
    ) {
    override var factorHeightForCustomViewer: Double = 0.75
    override fun oSetStringTitleForActionBar(): Int = R.string.ActivityItemOperationBatchText
    override fun aSetActivity(): BatchActivity = this
    override fun aFinishExecutePagingGenericRelationParent() {
        onEventMakeFilter = { item, find, it ->
            val filter: PagingData<MasterBatch> =
                when (item) {
                    FilterTypeActivityBatch.PRODUCT -> it.filter { s ->
                        if (s.item == null) s.item =
                            currentViewModel().viewModelGetProduct(s.idItem)
                        var data = false
                        if (s.item != null) {
                            data = s.item?.description!!.contains(
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
            val filter: PagingData<MasterItem> =
                when (item) {
                    FilterTypeActivityProduct.NAME -> it.filter { s ->
                        s.description.contains(
                            find,
                            true
                        )
                    }
                    FilterTypeActivityProduct.CODE -> it.filter { s -> s.code.contains(find, true) }
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
            val data: Flow<PagingData<MasterBatch>>? = when (parent == null) {
                true -> null
                false -> currentViewModel().viewModelGetViewAllPaging(parent?.id!!)
            }
            data
        }
        onEventSetParentFilterHasItems = { TypeFilterHasProductItems.BATCH }
    }

    override fun oHintForParent(): String = this.getString(R.string.HintParentForProduct)

    override fun aRelation(): MasterCompany? = getExtraParameter(MasterCompanyViewModel::class.java)
    override fun aCurrentListOfParent(): List<MasterItem>? {
        relation = aRelation()
        return when (relation == null) {
            true -> null
            false -> makeViewModel(MasterItemViewModel::class.java)
                .viewModelViewAllSimpleList(relation!!.id)
        }
    }
}