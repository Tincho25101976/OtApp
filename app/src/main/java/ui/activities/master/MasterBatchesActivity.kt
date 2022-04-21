package com.vsg.ot.ui.activities.master

import android.widget.RelativeLayout
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.helper.helper.Helper.Companion.or
import com.vsg.helper.helper.Helper.Companion.then
import com.vsg.helper.ui.util.CurrentBaseActivityPagingGenericRelationParent
import com.vsg.ot.R
import com.vsg.ot.ui.activities.master.util.FilterTypeActivityBatch
import com.vsg.ot.ui.activities.master.util.FilterTypeActivityCompany
import com.vsg.ot.ui.common.master.batch.UICRUDBatch
import common.model.master.batch.MasterBatch
import common.model.master.batch.MasterBatchDao
import common.model.master.batch.MasterBatchViewModel
import common.model.master.company.MasterCompany
import common.model.master.company.MasterCompanyDao
import common.model.master.company.MasterCompanyViewModel
import common.model.master.filter.TypeFilterHasCompanyItems
import common.model.master.item.MasterItem
import kotlinx.coroutines.flow.Flow

@ExperimentalStdlibApi
class MasterBatchesActivity :
    CurrentBaseActivityPagingGenericRelationParent
    <MasterBatchesActivity, MasterBatchViewModel, MasterBatchDao, MasterBatch, FilterTypeActivityBatch, UICRUDBatch<MasterBatchesActivity>,
            FilterTypeActivityCompany, MasterCompanyViewModel, MasterCompanyDao, MasterCompany, TypeFilterHasCompanyItems>
        (
        MasterBatchViewModel::class.java,
        MasterCompanyViewModel::class.java,
        FilterTypeActivityBatch::class.java,
        FilterTypeActivityCompany::class.java
    ) {
    private lateinit var item: MasterItem

    override fun oSetStringTitleForActionBar(): Int? = R.string.ActivityItemOperationBatchText
    override fun aSetActivity(): MasterBatchesActivity = this
    override fun aFinishExecutePagingGenericRelationParent() {
        onEventMakeFilter = { item, find, it ->
            val filter: PagingData<MasterBatch> =
                when (item) {
                    FilterTypeActivityBatch.PRODUCT -> it.filter { s ->
                        if (s.item == null) s.item =
                            currentViewModel().viewModelGetProduct(s.idItem)
                        var data = false
                        if (s.item != null) {
                            data = s.item?.description!!.contains(find, true)
                        }
                        data
                    }
                    FilterTypeActivityBatch.VALUE_CODE -> it.filter { s ->
                        s.valueCode.contains(find, true)
                    }
                    else -> it
                }
            filter
        }
        onEventMakeFilterResult = { item, find, it ->
            val filter: PagingData<MasterCompany> =
                when (item) {
                    FilterTypeActivityCompany.NAME -> it.filter { s -> s.description.contains(find, true) }
                    else -> it
                }
            filter
        }
        onEventSetCRUDForApply = { context, operation ->
            UICRUDBatch(
                context,
                operation,
                this@MasterBatchesActivity.item
            ).apply { factorHeight = 0.8 }
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
        onEventSetParentFilterHasItems = { TypeFilterHasCompanyItems.PRODUCT }
    }
    override fun aCurrentListOfParent(): List<MasterCompany> = makeViewModel(MasterCompanyViewModel::class.java)
        .viewModelViewAllSimpleList()
}