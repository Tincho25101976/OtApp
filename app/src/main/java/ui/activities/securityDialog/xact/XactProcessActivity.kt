package com.vsg.ot.ui.activities.securityDialog.xact

import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.helper.ui.util.CurrentBaseActivityPagingGeneric
import com.vsg.ot.R
import com.vsg.ot.common.model.securityDialog.xact.process.XactProcess
import com.vsg.ot.common.model.securityDialog.xact.process.XactProcessDao
import com.vsg.ot.common.model.securityDialog.xact.process.XactProcessViewModel
import com.vsg.ot.ui.activities.securityDialog.xact.util.FilterTypeActivityXactProcess
import com.vsg.ot.ui.common.securityDigital.xact.process.UICRUDXactProcess

@ExperimentalStdlibApi
class XactProcessActivity :
    CurrentBaseActivityPagingGeneric<XactProcessActivity, XactProcessViewModel,
            XactProcessDao, XactProcess,
            FilterTypeActivityXactProcess, UICRUDXactProcess<XactProcessActivity>>(
        XactProcessViewModel::class.java,
        FilterTypeActivityXactProcess::class.java
    ) {

    override fun oSetStringTitleForActionBar(): Int = R.string.ActivityMainCommandProcess
    override fun aFinishExecute() {
        onEventMakeFilter = { item, find, it ->
            val filter: PagingData<XactProcess> =
                when (item) {
                    FilterTypeActivityXactProcess.NAME -> it.filter { s ->
                        s.valueCode.contains(
                            find,
                            true
                        )
                    }
                    else -> it
                }
            filter
        }
        onEventSetCRUDForApply = { context, operation -> UICRUDXactProcess(context, operation) }
    }
}