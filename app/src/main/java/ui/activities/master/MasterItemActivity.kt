package com.vsg.ot.ui.activities.master

import android.widget.RelativeLayout
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.ot.ui.activities.master.util.FilterTypeActivityCompany
import com.vsg.ot.ui.activities.master.util.FilterTypeActivityItem
import com.vsg.ot.ui.common.master.product.UICRUDItem
import com.vsg.helper.ui.util.CurrentBaseActivityPagingGenericRelationParent
import com.vsg.ot.R
import common.model.master.company.MasterCompany
import common.model.master.company.MasterCompanyDao
import common.model.master.company.MasterCompanyViewModel
import common.model.master.filter.TypeFilterHasCompanyItems
import common.model.master.item.MasterItem
import common.model.master.item.MasterItemDao
import common.model.master.item.MasterItemViewModel
import kotlinx.coroutines.flow.Flow

@ExperimentalStdlibApi
class MasterItemActivity :
    CurrentBaseActivityPagingGenericRelationParent
        <MasterItemActivity, MasterItemViewModel, MasterItemDao, MasterItem, FilterTypeActivityItem, UICRUDItem<MasterItemActivity>,
                FilterTypeActivityCompany, MasterCompanyViewModel, MasterCompanyDao, MasterCompany, TypeFilterHasCompanyItems>
        (
        MasterItemViewModel::class.java,
        MasterCompanyViewModel::class.java,
        FilterTypeActivityItem::class.java,
        FilterTypeActivityCompany::class.java
    ) {

    override fun oSetStringTitleForActionBar(): Int = R.string.ActivityMasterItemText
    override fun aSetActivity(): MasterItemActivity = this
    override fun oSetSwipeMenuItems(): Int = R.layout.swipe_menu_master_item
    override fun aFinishExecutePagingGenericRelationParent() {
        onEventMakeFilter = { item, find, it ->
            val filter: PagingData<MasterItem> =
                when (item) {
                    FilterTypeActivityItem.NAME -> it.filter { s -> s.description.contains(find, true) }
                    FilterTypeActivityItem.CODE -> it.filter { s -> s.valueCode.contains(find, true) }
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
            UICRUDItem(
                context,
                operation,
                currentViewModel(),
                parent!!
            ).apply { factorHeight = 0.8 }
        }
        onEventSwipeGetViewForMenu = {
            it.findViewById<RelativeLayout>(R.id.SwipeMenuProductItemBatch)
                .setOnClickListener {
                    if (getItem() != null) loadActivity(
                        MasterBatchActivity::class.java, getItem()!!, extra = parent
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
            val data: Flow<PagingData<MasterItem>>? = when (parent == null) {
                true -> null
                false -> currentViewModel().viewModelGetViewAllPaging(parent?.id!!)
            }
            data
        }
        onEventSetParentFilterHasItems = { TypeFilterHasCompanyItems.PRODUCT }
    }
    override fun aCurrentListOfParent(): List<MasterCompany> = makeViewModel(MasterCompanyViewModel::class.java)
        .viewModelViewAllSimpleList()

//    init {
//        onEventGetIdRelationFromIntent = {
//            company = getParent(CompanyViewModel::class.java)
//            if (company == null) {
//                val items =
//                    this.makeViewModel(CompanyViewModel::class.java).viewModelViewAllSimpleList()
//                if (items.count() > 1) {
//                    UIAlertDialogResultCompany(
//                        this,
//                        this.makeViewModel(CompanyViewModel::class.java)
//                    ).apply {
//                        factorHeight = 0.45
//                        onEventClickOK = { setParent(it) }
//                        onEventGetItemAfterBind = { t ->
//                            t.hasItems = currentViewModel().viewModelViewHasItems(t.id)
//                            t
//                        }
//                        make()
//                    }
//                }
//                if (items.count() == 1) setParent(items.first())
//            }
//        }
//    }

//    override fun aFinishExecute() {
//        onEventMakeFilter = { item, find, it ->
//            val filter: PagingData<MasterItem> =
//                when (item) {
//                    FilterTypeActivityProduct.NAME -> it.filter { s -> s.name.contains(find, true) }
//                    FilterTypeActivityProduct.CODE -> it.filter { s -> s.code.contains(find, true) }
//                    FilterTypeActivityProduct.PROVIDER_CODE -> it.filter { s ->
//                        s.providerCode.contains(find, true)
//                    }
//                    else -> it
//                }
//            filter
//        }
//        onEventSetCRUDForApply = { context, operation ->
//            UICRUDProduct(
//                context,
//                operation,
//                currentViewModel(),
//                company!!
//            ).apply { factorHeight = 0.8 }
//        }
//        onEventSwipeGetViewForMenu = {
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
//            it.findViewById<RelativeLayout>(R.id.SwipeMenuProductItemBatch)
//                .setOnClickListener {
//                    if (getItem() != null) loadActivity(
//                        BatchActivity::class.java, getItem()!!, extra = company
//                    )
//                }
//        }
//        onEventGetListTextSearch = {
//            val data = when (company == null) {
//                true -> MutableLiveData()
//                false -> currentViewModel().viewModelGetAllTextSearch(company?.id!!)
//            }
//            data
//        }
//        onEventGetViewAllPaging = {
//            val data: Flow<PagingData<MasterItem>>? = when (company == null) {
//                true -> null
//                false -> currentViewModel().viewModelGetViewAllPaging(company?.id!!)
//            }
//            data
//        }
//        setParent(this.company)
//    }
//    private fun setParent(it: MasterCompany?) {
//        if (it == null) return
//        company = it
//        if (company != null) {
//            fillTextSearch()
//            fillAdapter()
//        }
//    }
}