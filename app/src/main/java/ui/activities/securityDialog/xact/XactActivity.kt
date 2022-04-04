package ui.activities.securityDialog.xact

import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.helper.ui.util.CurrentBaseActivityPagingGeneric
import com.vsg.ot.R
import common.model.securityDialog.xact.Xact
import common.model.securityDialog.xact.XactDao
import common.model.securityDialog.xact.XactViewModel
import ui.activities.securityDialog.xact.util.FilterTypeActivityXact
import ui.common.xact.UICRUDXact

@ExperimentalStdlibApi
class XactActivity:
    CurrentBaseActivityPagingGeneric<XactActivity, XactViewModel, XactDao, Xact, FilterTypeActivityXact, UICRUDXact<XactActivity>>(
        XactViewModel::class.java,
        FilterTypeActivityXact::class.java
    ) {

    override fun oSetStringTitleForActionBar(): Int = R.string.ActivityItemProductCategoryText
    override fun aFinishExecute() {
        onEventMakeFilter = { item, find, it ->
            val filter: PagingData<Xact> =
                when (item) {
                    FilterTypeActivityXact.NAME -> it.filter { s ->
                        s.caption.contains(
                            find,
                            true
                        )
                    }
                    else -> it
                }
            filter
        }
        onEventSetCRUDForApply = { context, operation -> UICRUDXact(context, operation) }
    }
}