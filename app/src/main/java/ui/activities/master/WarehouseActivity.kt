package com.vsg.agendaandpublication.ui.activities.itemOperation

import android.widget.RelativeLayout
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.agendaandpublication.R
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.warehouse.Warehouse
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.warehouse.WarehouseDao
import com.vsg.agendaandpublication.common.model.itemOperation.wharehouse.warehouse.WarehouseViewModel
import com.vsg.agendaandpublication.common.model.itemProduct.company.Company
import com.vsg.agendaandpublication.common.model.itemProduct.company.CompanyDao
import com.vsg.agendaandpublication.common.model.itemProduct.company.CompanyViewModel
import com.vsg.agendaandpublication.common.model.itemProduct.filter.TypeFilterHasCompanyItems
import com.vsg.agendaandpublication.ui.activities.itemOperation.util.FilterTypeActivityWarehouse
import com.vsg.agendaandpublication.ui.activities.itemProducto.PriceActivity
import com.vsg.agendaandpublication.ui.activities.itemProducto.util.FilterTypeActivityCompany
import com.vsg.agendaandpublication.ui.common.itemOperation.warehouse.UICRUDWarehouse
import com.vsg.utilities.ui.util.CurrentBaseActivityPagingGenericRelationParent
import kotlinx.coroutines.flow.Flow

@ExperimentalStdlibApi
class WarehouseActivity :
    CurrentBaseActivityPagingGenericRelationParent<WarehouseActivity, WarehouseViewModel, WarehouseDao, Warehouse, FilterTypeActivityWarehouse, UICRUDWarehouse<WarehouseActivity>,
            FilterTypeActivityCompany, CompanyViewModel, CompanyDao, Company, TypeFilterHasCompanyItems>(
        WarehouseViewModel::class.java, CompanyViewModel::class.java,
        FilterTypeActivityWarehouse::class.java, FilterTypeActivityCompany::class.java
    ) {
    override fun oSetStringTitleForActionBar(): Int = R.string.ActivityItemOperationWarehouseText
    override fun aSetActivity(): WarehouseActivity = this
    override fun oSetSwipeMenuItems(): Int = R.layout.swipe_menu_warehouse_item
    override fun aFinishExecutePagingGenericRelationParent() {
        onEventMakeFilter = { item, find, it ->
            val filter: PagingData<Warehouse> =
                when (item) {
                    FilterTypeActivityWarehouse.NAME -> it.filter { s ->
                        s.name.contains(
                            find,
                            true
                        )
                    }
                    FilterTypeActivityWarehouse.PREFIX -> it.filter { s ->
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
            val filter: PagingData<Company> =
                when (item) {
                    FilterTypeActivityCompany.NAME -> it.filter { s -> s.name.contains(find, true) }
                    else -> it
                }
            filter
        }
        onEventSetCRUDForApply = { context, operation ->
            UICRUDWarehouse(
                context,
                operation,
                parent!!
            ).apply { factorHeight = 0.8 }
        }
        onEventSwipeGetViewForMenu = {
            it.findViewById<RelativeLayout>(R.id.SwipeMenuOperationItemSection)
                .setOnClickListener {
                    if (getItem() != null) loadActivity(
                        SectionActivity::class.java, getItem()!!, extra = parent
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
            val data: Flow<PagingData<Warehouse>>? = when (parent == null) {
                true -> null
                false -> currentViewModel().viewModelGetViewAllPaging(parent?.id!!)
            }
            data
        }
        onEventSetParentFilterHasItems = { TypeFilterHasCompanyItems.WAREHOUSE }
    }
    override fun aCurrentListOfParent(): List<Company> = makeViewModel(CompanyViewModel::class.java)
        .viewModelViewAllSimpleList()
}