package com.vsg.agendaandpublication.ui.activities.itemProducto

import android.widget.RelativeLayout
import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.agendaandpublication.R
import com.vsg.agendaandpublication.common.model.itemProduct.company.Company
import com.vsg.agendaandpublication.common.model.itemProduct.company.CompanyDao
import com.vsg.agendaandpublication.common.model.itemProduct.company.CompanyViewModel
import com.vsg.agendaandpublication.ui.activities.itemOperation.MoneyActivity
import com.vsg.agendaandpublication.ui.activities.itemOperation.WarehouseActivity
import com.vsg.agendaandpublication.ui.activities.itemPerson.PersonActivity
import com.vsg.agendaandpublication.ui.activities.itemProducto.util.FilterSearchCompany
import com.vsg.agendaandpublication.ui.activities.itemProducto.util.FilterTypeActivityCompany
import com.vsg.agendaandpublication.ui.common.itemProduct.company.UICRUDCompany
import com.vsg.utilities.ui.util.CurrentBaseActivityPagingGeneric

@ExperimentalStdlibApi
class CompanyActivity :
    CurrentBaseActivityPagingGeneric<CompanyActivity, CompanyViewModel, CompanyDao, Company, FilterTypeActivityCompany, UICRUDCompany<CompanyActivity>>(
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