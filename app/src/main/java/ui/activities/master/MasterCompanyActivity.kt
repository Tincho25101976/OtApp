package com.vsg.ot.ui.activities.master

import android.widget.RelativeLayout
import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.helper.ui.util.CurrentBaseActivityPagingGeneric
import com.vsg.ot.R
import com.vsg.ot.ui.activities.master.util.FilterTypeActivityCompany
import com.vsg.ot.ui.common.master.company.UICRUDCompany
import common.model.master.company.MasterCompany
import common.model.master.company.MasterCompanyDao
import common.model.master.company.MasterCompanyViewModel

@ExperimentalStdlibApi
class MasterCompanyActivity :
    CurrentBaseActivityPagingGeneric<MasterCompanyActivity, MasterCompanyViewModel,
            MasterCompanyDao, MasterCompany,
            FilterTypeActivityCompany, UICRUDCompany<MasterCompanyActivity>>(
        MasterCompanyViewModel::class.java,
        FilterTypeActivityCompany::class.java
    ) {
    override fun oSetSwipeMenuItems(): Int = R.layout.swipe_menu_master_company
    override fun oSetStringTitleForActionBar(): Int = R.string.ActivityMasterCompanyText
    override fun aFinishExecute() {
        onEventMakeFilter = { item, find, it ->
            val filter: PagingData<MasterCompany> =
                when (item) {
                    FilterTypeActivityCompany.NAME -> it.filter { s ->
                        s.valueCode.contains(
                            find,
                            true
                        )
                    }
                    FilterTypeActivityCompany.DESCRIPTION -> it.filter { s ->
                        s.description.contains(
                            find,
                            true
                        )
                    }
                    else -> it
                }
            filter
        }
        onEventSetCRUDForApply = { context, operation -> UICRUDCompany(context, operation) }
        onEventSwipeGetViewForMenu = {
            it.findViewById<RelativeLayout>(R.id.SwipeMenuMasterCompany)
                .setOnClickListener {
                    if (getItem() != null) {
                        loadActivity(MasterItemActivity::class.java, getItem()!!)
                    }
                }
            it.findViewById<RelativeLayout>(R.id.SwipeMenuCompanyItemWarehouse)
                .setOnClickListener {
                    if (getItem() != null) {
                        loadActivity(MasterWarehouseActivity::class.java, getItem()!!)
                    }
                }
        }
    }
}