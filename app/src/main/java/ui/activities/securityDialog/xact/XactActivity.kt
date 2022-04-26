package ui.activities.securityDialog.xact

import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.helper.ui.util.CurrentBaseActivityPagingGeneric
import com.vsg.ot.R
import com.vsg.ot.common.model.securityDialog.xact.xact.Xact
import com.vsg.ot.common.model.securityDialog.xact.xact.XactDao
import com.vsg.ot.common.model.securityDialog.xact.xact.XactViewModel
import com.vsg.ot.ui.common.securityDigital.xact.xact.UICRUDXact
import ui.activities.securityDialog.xact.util.FilterTypeActivityXact

@ExperimentalStdlibApi
class XactActivity :
    CurrentBaseActivityPagingGeneric<XactActivity, XactViewModel,
            XactDao, Xact,
            FilterTypeActivityXact, UICRUDXact<XactActivity>>(
        XactViewModel::class.java,
        FilterTypeActivityXact::class.java
    ) {

    override fun oSetStringTitleForActionBar(): Int = R.string.ActivityMainCommandRecord
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