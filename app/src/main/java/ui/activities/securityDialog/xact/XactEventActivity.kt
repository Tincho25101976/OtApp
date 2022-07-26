package com.vsg.ot.ui.activities.securityDialog.xact

import android.view.Menu
import android.view.MenuItem
import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.helper.ui.util.CurrentBaseActivityPagingGeneric
import com.vsg.ot.R
import com.vsg.ot.common.model.securityDialog.xact.event.XactEvent
import com.vsg.ot.common.model.securityDialog.xact.event.XactEventDao
import com.vsg.ot.common.model.securityDialog.xact.event.XactEventViewModel
import com.vsg.ot.ui.activities.securityDialog.xact.util.FilterTypeActivityXactProcess
import com.vsg.ot.ui.common.securityDigital.xact.event.UICRUDXactEvent
import com.vsg.ot.ui.common.securityDigital.xact.event.UIUpdateDataXactEvent

@ExperimentalStdlibApi
class XactEventActivity :
    CurrentBaseActivityPagingGeneric<XactEventActivity, XactEventViewModel,
            XactEventDao, XactEvent,
            FilterTypeActivityXactProcess, UICRUDXactEvent<XactEventActivity>>(
        XactEventViewModel::class.java,
        FilterTypeActivityXactProcess::class.java
    ) {

    override fun oSetStringTitleForActionBar(): Int = R.string.ActivityMainCommandProcess
    override fun aFinishExecute() {
        onEventMakeFilter = { item, find, it ->
            val filter: PagingData<XactEvent> =
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
        onEventSetCRUDForApply = { context, operation -> UICRUDXactEvent(context, operation) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_update_source, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.MenuActionUpdateSource -> {
                loadActivity(UIUpdateDataXactEvent::class.java)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}