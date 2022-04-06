package com.vsg.ot.ui.activities.master

import android.widget.RelativeLayout
import androidx.paging.PagingData
import com.vsg.ot.ui.activities.master.util.FilterTypeActivityCompany

import com.vsg.helper.ui.util.CurrentBaseActivityPagingGeneric
import common.model.master.company.MasterCompany
import common.model.master.company.MasterCompanyDao
import common.model.master.company.MasterCompanyViewModel

@ExperimentalStdlibApi
class CompanyActivity :
    CurrentBaseActivityPagingGeneric<CompanyActivity, MasterCompanyViewModel, MasterCompanyDao, MasterCompany, FilterTypeActivityCompany, UICRUDCompany<CompanyActivity>>(
        CompanyViewModel::class.java,
        FilterTypeActivityCompany::class.java
    ) {
    override fun oSetSwipeMenuItems(): Int = R.layout.swipe_menu_company_item
    override fun oSetStringTitleForActionBar(): Int = R.string.ActivityItemProductCompanyText
    override fun aFinishExecute() {
        onEventMakeFilter = { item, find, it ->
            val filter: PagingData<Company> =
                when (item) {
                    FilterTypeActivityCompany.NAME -> it.filter { s ->
                        s.name.contains(
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
            it.findViewById<RelativeLayout>(R.id.SwipeMenuCompanyItemProduct)
                .setOnClickListener {
                    if (getItem() != null) {
                        loadActivity(ProductActivity::class.java, getItem()!!)
                    }
                }
            it.findViewById<RelativeLayout>(R.id.SwipeMenuCompanyItemMoney)
                .setOnClickListener {
                    if (getItem() != null) {
                        loadActivity(MoneyActivity::class.java, getItem()!!)
                    }
                }
            it.findViewById<RelativeLayout>(R.id.SwipeMenuCompanyItemWarehouse)
                .setOnClickListener {
                    if (getItem() != null) {
                        loadActivity(WarehouseActivity::class.java, getItem()!!)
                    }
                }
            it.findViewById<RelativeLayout>(R.id.SwipeMenuCompanyItemPerson)
                .setOnClickListener {
                    if (getItem() != null) {
                        loadActivity(PersonActivity::class.java, getItem()!!)
                    }
                }
        }
    }
}