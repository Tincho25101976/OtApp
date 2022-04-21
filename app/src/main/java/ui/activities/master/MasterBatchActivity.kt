package com.vsg.ot.ui.activities.master

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.helper.helper.exception.HelperException.Companion.throwException
import com.vsg.helper.ui.util.CurrentBaseActivityPagingGenericRelationParentWithRelation
import com.vsg.ot.R
import com.vsg.ot.ui.activities.master.util.FilterTypeActivityBatch
import com.vsg.ot.ui.activities.master.util.FilterTypeActivityItem
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
class MasterBatchActivity :
    CurrentBaseActivityPagingGenericRelationParentWithRelation
        <MasterBatchActivity, MasterBatchViewModel, MasterBatchDao, MasterBatch, FilterTypeActivityBatch, UICRUDBatch<MasterBatchActivity>,
            FilterTypeActivityItem, MasterItemViewModel, MasterItemDao, MasterItem, TypeFilterHasProductItems, MasterCompany>(
        MasterBatchViewModel::class.java,
        MasterItemViewModel::class.java,
        FilterTypeActivityBatch::class.java,
        FilterTypeActivityItem::class.java
    ) {
//    //region handler
//    var onEventGetCompany: ((MasterCompany) -> Unit)? = null
//    //endregion

    //region properties
    override var factorHeightForCustomViewer: Double = 0.75
    //endregion

    override fun oSetStringTitleForActionBar(): Int = R.string.ActivityItemOperationBatchText
    override fun aSetActivity(): MasterBatchActivity = this
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
                    FilterTypeActivityItem.NAME -> it.filter { s ->
                        s.description.contains(
                            find,
                            true
                        )
                    }
                    FilterTypeActivityItem.CODE -> it.filter { s ->
                        s.valueCode.contains(
                            find,
                            true
                        )
                    }
                    else -> it
                }
            filter
        }
        onEventSetCRUDForApply = { context, operation ->
            if(parent == null) "No hay artículos definidos para esta empresa...".throwException()
            UICRUDBatch(
                context,
                operation,
                parent!!
            ).apply { factorHeight = 0.8 }
        }
        onEventSwipeGetViewForMenu = {

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
    override fun aRelation(): MasterCompany? {
        return getExtraParameter(MasterCompanyViewModel::class.java)
    }

    override fun aCurrentListOfParent(): List<MasterItem>? {
        relation = aRelation()
        return when (relation == null) {
            true -> null
            false -> makeViewModel(MasterItemViewModel::class.java)
                .viewModelViewAllSimpleList(relation!!.id)
        }
    }
    //endregion
}