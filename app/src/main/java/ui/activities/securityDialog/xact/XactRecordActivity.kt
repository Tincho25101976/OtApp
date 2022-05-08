package com.vsg.ot.ui.activities.securityDialog.xact

import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.helper.ui.util.CurrentBaseActivityPagingGeneric
import com.vsg.ot.R
import com.vsg.ot.common.model.securityDialog.xact.record.XactRecord
import com.vsg.ot.common.model.securityDialog.xact.record.XactRecordDao
import com.vsg.ot.common.model.securityDialog.xact.record.XactRecordViewModel
import com.vsg.ot.ui.common.securityDigital.xact.record.UICRUDXactRecord
import ui.activities.securityDialog.xact.util.FilterTypeActivityXactRecord

@ExperimentalStdlibApi
class XactRecordActivity :
    CurrentBaseActivityPagingGeneric<XactRecordActivity, XactRecordViewModel,
            XactRecordDao, XactRecord,
            FilterTypeActivityXactRecord, UICRUDXactRecord<XactRecordActivity>>(
        XactRecordViewModel::class.java,
        FilterTypeActivityXactRecord::class.java
    ) {

    override fun oSetStringTitleForActionBar(): Int = R.string.ActivityMainCommandRecord
    override fun aFinishExecute() {
        onEventMakeFilter = { item, find, it ->
            val filter: PagingData<XactRecord> =
                when (item) {
                    FilterTypeActivityXactRecord.NAME -> it.filter { s ->
                        s.caption.contains(
                            find,
                            true
                        )
                    }
                    else -> it
                }
            filter
        }
        onEventSetCRUDForApply = { context, operation -> UICRUDXactRecord(context, operation) }
    }
}