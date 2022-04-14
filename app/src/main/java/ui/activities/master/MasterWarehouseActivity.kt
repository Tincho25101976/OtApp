package com.vsg.ot.ui.activities.master

import android.widget.RelativeLayout
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.helper.ui.util.CurrentBaseActivityPagingGenericRelationParent
import com.vsg.ot.R
import com.vsg.ot.ui.activities.master.util.FilterTypeActivityCompany
import com.vsg.ot.ui.activities.master.util.FilterTypeActivityWarehouse
import com.vsg.ot.ui.common.master.warehouse.UICRUDWarehouse
import common.model.master.company.MasterCompany
import common.model.master.company.MasterCompanyDao
import common.model.master.company.MasterCompanyViewModel
import common.model.master.filter.TypeFilterHasCompanyItems
import common.model.master.warehouse.MasterWarehouse
import common.model.master.warehouse.MasterWarehouseDao
import common.model.master.warehouse.MasterWarehouseViewModel
import kotlinx.coroutines.flow.Flow

@ExperimentalStdlibApi
class MasterWarehouseActivity :
    CurrentBaseActivityPagingGenericRelationParent<MasterWarehouseActivity, MasterWarehouseViewModel, MasterWarehouseDao, MasterWarehouse, FilterTypeActivityWarehouse, UICRUDWarehouse<MasterWarehouseActivity>,
            FilterTypeActivityCompany, MasterCompanyViewModel, MasterCompanyDao, MasterCompany, TypeFilterHasCompanyItems>(
        MasterWarehouseViewModel::class.java, MasterCompanyViewModel::class.java,
        FilterTypeActivityWarehouse::class.java, FilterTypeActivityCompany::class.java
    ) {
    override fun oSetStringTitleForActionBar(): Int = R.string.ActivityItemOperationWarehouseText
    override fun aSetActivity(): MasterWarehouseActivity = this
    override fun oSetSwipeMenuItems(): Int = R.layout.swipe_menu_master_warehouse
    override fun aFinishExecutePagingGenericRelationParent() {
        onEventMakeFilter = { item, find, it ->
            val filter: PagingData<MasterWarehouse> =
                when (item) {
                    FilterTypeActivityWarehouse.NAME -> it.filter { s ->
                        s.description.contains(
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
            val filter: PagingData<MasterCompany> =
                when (item) {
                    FilterTypeActivityCompany.NAME -> it.filter { s ->
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
                        MasterSectionActivity::class.java, getItem()!!, extra = parent
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
            val data: Flow<PagingData<MasterWarehouse>>? = when (parent == null) {
                true -> null
                false -> currentViewModel().viewModelGetViewAllPaging(parent?.id!!)
            }
            data
        }
        onEventSetParentFilterHasItems = { TypeFilterHasCompanyItems.WAREHOUSE }
    }

    override fun aCurrentListOfParent(): List<MasterCompany> =
        makeViewModel(MasterCompanyViewModel::class.java)
            .viewModelViewAllSimpleList()
}