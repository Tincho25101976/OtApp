package com.vsg.ot.ui.activities.master

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.helper.ui.util.CurrentBaseActivityPagingGenericRelationParentWithRelation
import com.vsg.ot.R
import com.vsg.ot.ui.activities.master.util.FilterTypeActivitySection
import com.vsg.ot.ui.activities.master.util.FilterTypeActivityWarehouse
import com.vsg.ot.ui.common.master.warehouse.UICRUDSection
import common.model.master.company.MasterCompany
import common.model.master.company.MasterCompanyViewModel
import common.model.master.filter.TypeFilterHasWarehouseItems
import common.model.master.section.MasterSection
import common.model.master.section.MasterSectionDao
import common.model.master.section.MasterSectionViewModel
import common.model.master.warehouse.MasterWarehouse
import common.model.master.warehouse.MasterWarehouseDao
import common.model.master.warehouse.MasterWarehouseViewModel
import kotlinx.coroutines.flow.Flow

@ExperimentalStdlibApi
class MasterSectionActivity :
    CurrentBaseActivityPagingGenericRelationParentWithRelation<
            MasterSectionActivity, MasterSectionViewModel, MasterSectionDao, MasterSection, FilterTypeActivitySection, UICRUDSection<MasterSectionActivity>,
            FilterTypeActivityWarehouse, MasterWarehouseViewModel, MasterWarehouseDao, MasterWarehouse, TypeFilterHasWarehouseItems,
            MasterCompany>(
        MasterSectionViewModel::class.java,
        MasterWarehouseViewModel::class.java,
        FilterTypeActivitySection::class.java,
        FilterTypeActivityWarehouse::class.java,
    ) {
    override fun oSetStringTitleForActionBar(): Int =
        R.string.ActivityItemOperationSectionText

    override fun aSetActivity(): MasterSectionActivity = this
    override fun aRelation(): MasterCompany? = getExtraParameter(MasterCompanyViewModel::class.java)
    override fun aFinishExecutePagingGenericRelationParent() {
        onEventMakeFilter = { item, find, it ->
            val filter: PagingData<MasterSection> =
                when (item) {
                    FilterTypeActivitySection.NAME -> it.filter { s ->
                        s.description.contains(
                            find,
                            true
                        )
                    }
                    FilterTypeActivitySection.PREFIX -> it.filter { s ->
                        s.prefix.contains(
                            find,
                            true
                        )
                    }
                    else -> it
                }
            filter
        }
        onEventMakeFilterResult = { item, find, it ->
            val filter: PagingData<MasterWarehouse> =
                when (item) {
                    FilterTypeActivityWarehouse.NAME -> it.filter { s ->
                        s.description.contains(
                            find,
                            true
                        )
                    }
                    else -> it
                }
            filter
        }
        onEventSetCRUDForApply = { context, operation ->
            UICRUDSection(
                context,
                operation,
                parent!!
            ).apply { factorHeight = 0.8 }
        }
        onEventSwipeGetViewForMenu = {}
        onEventGetListTextSearch = {
            val data = when (parent == null) {
                true -> MutableLiveData()
                false -> currentViewModel().viewModelGetAllTextSearch(parent?.id!!)
            }
            data
        }
        onEventGetViewAllPaging = {
            val data: Flow<PagingData<MasterSection>>? = when (parent == null) {
                true -> null
                false -> currentViewModel().viewModelGetViewAllPaging(parent?.id!!)
            }
            data
        }
        onEventSetParentFilterHasItems = { TypeFilterHasWarehouseItems.SECTION }
    }

    override fun oHintForParent(): String = this.getString(R.string.HintParentForCompany)
    override fun aCurrentListOfParent(): List<MasterWarehouse>? {
        this.relation = aRelation()
        return when (relation == null) {
            true -> null
            false -> makeViewModel(MasterWarehouseViewModel::class.java)
                .viewModelViewAllSimpleList(relation!!.id)
        }
    }
}