package com.vsg.agendaandpublication.ui.activities.itemOperation

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.agendaandpublication.R
import com.vsg.agendaandpublication.common.model.itemOperation.filter.TypeFilterHasWarehouseItems
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.section.Section
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.section.SectionDao
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.section.SectionViewModel
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.warehouse.Warehouse
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.warehouse.WarehouseDao
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.warehouse.WarehouseViewModel
import com.vsg.agendaandpublication.common.model.itemProduct.company.Company
import com.vsg.agendaandpublication.common.model.itemProduct.company.CompanyViewModel
import com.vsg.agendaandpublication.ui.activities.itemOperation.util.FilterTypeActivitySection
import com.vsg.agendaandpublication.ui.activities.itemOperation.util.FilterTypeActivityWarehouse
import com.vsg.agendaandpublication.ui.common.itemOperation.warehouse.UICRUDSection
import com.vsg.utilities.ui.util.CurrentBaseActivityPagingGenericRelationParentWithRelation
import kotlinx.coroutines.flow.Flow

@ExperimentalStdlibApi
class SectionActivity :
    CurrentBaseActivityPagingGenericRelationParentWithRelation<
            SectionActivity, SectionViewModel, SectionDao, Section, FilterTypeActivitySection, UICRUDSection<SectionActivity>,
            FilterTypeActivityWarehouse, WarehouseViewModel, WarehouseDao, Warehouse, TypeFilterHasWarehouseItems,
            Company>(
        SectionViewModel::class.java,
        WarehouseViewModel::class.java,
        FilterTypeActivitySection::class.java,
        FilterTypeActivityWarehouse::class.java,
    ) {
    override fun oSetStringTitleForActionBar(): Int =
        R.string.ActivityItemOperationSectionText

    override fun aSetActivity(): SectionActivity = this
    override fun aRelation(): Company? = getExtraParameter(CompanyViewModel::class.java)
    override fun aFinishExecutePagingGenericRelationParent() {
        onEventMakeFilter = { item, find, it ->
            val filter: PagingData<Section> =
                when (item) {
                    FilterTypeActivitySection.NAME -> it.filter { s -> s.name.contains(find, true) }
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
            val filter: PagingData<Warehouse> =
                when (item) {
                    FilterTypeActivityWarehouse.NAME -> it.filter { s ->
                        s.name.contains(
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
            val data: Flow<PagingData<Section>>? = when (parent == null) {
                true -> null
                false -> currentViewModel().viewModelGetViewAllPaging(parent?.id!!)
            }
            data
        }
        onEventSetParentFilterHasItems = { TypeFilterHasWarehouseItems.SECTION }
    }
    override fun oHintForParent(): String = this.getString(R.string.HintParentForCompany)
    override fun aCurrentListOfParent(): List<Warehouse>? {
        this.relation = aRelation()
        return when (relation == null) {
            true -> null
            false -> makeViewModel(WarehouseViewModel::class.java)
                .viewModelViewAllSimpleList(relation!!.id)
        }
    }
}