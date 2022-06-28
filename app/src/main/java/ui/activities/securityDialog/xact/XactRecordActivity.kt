package com.vsg.ot.ui.activities.securityDialog.xact

import android.view.Menu
import android.view.MenuItem
import android.widget.RelativeLayout
import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.helper.common.export.ExportType
import com.vsg.helper.ui.util.CurrentBaseActivityPagingGenericParseExport
import com.vsg.helper.ui.util.CurrentBaseActivityPagingGenericParseExportReport
import com.vsg.ot.R
import com.vsg.ot.common.model.securityDialog.xact.record.XactRecord
import com.vsg.ot.common.model.securityDialog.xact.record.XactRecordDao
import com.vsg.ot.common.model.securityDialog.xact.record.XactRecordViewModel
import com.vsg.ot.ui.common.securityDigital.xact.record.UICRUDXactRecord
import com.vsg.ot.ui.common.securityDigital.xact.record.UIUpdateDataXactRecord
import ui.activities.securityDialog.xact.util.FilterTypeActivityXactRecord

@ExperimentalStdlibApi
class XactRecordActivity :
    CurrentBaseActivityPagingGenericParseExportReport<XactRecordActivity, XactRecordViewModel,
            XactRecordDao, XactRecord,
            FilterTypeActivityXactRecord, UICRUDXactRecord<XactRecordActivity>>(
        XactRecordViewModel::class.java,
        FilterTypeActivityXactRecord::class.java
    ) {

    override fun oSetStringTitleForActionBar(): Int = R.string.ActivityMainCommandRecord
    override fun oSetSwipeMenuItems(): Int = R.layout.swipe_menu_report_export
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
        onEventSwipeGetViewForMenu = {
            it.findViewById<RelativeLayout>(R.id.SwipeMenuReportPDF)
                .setOnClickListener {
//                    if (getItem() != null) sendReport(getItem()!!, ExportType.PDF)
                    if (getItem() != null) openReport(getItem()!!, ExportType.PDF)
                }
            it.findViewById<RelativeLayout>(R.id.SwipeMenuReportXML)
                .setOnClickListener {
                    if (getItem() != null) sendExport(getItem()!!, ExportType.XML)
                }
            it.findViewById<RelativeLayout>(R.id.SwipeMenuReportJson)
                .setOnClickListener {
                    if (getItem() != null) sendExport(getItem()!!, ExportType.JSON)
                }
        }
        onSetTitleReport = {
            "Reporte de condición insegura"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_update_source, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.MenuActionUpdateSource -> loadActivity(UIUpdateDataXactRecord::class.java)
        }
        return super.onOptionsItemSelected(item)
    }
}