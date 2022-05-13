package com.vsg.ot.ui.activities.securityDialog.xact

import android.view.MenuItem
import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.helper.ui.util.CurrentBaseActivityPagingGeneric
import com.vsg.ot.R
import com.vsg.ot.common.model.securityDialog.xact.sector.XactSector
import com.vsg.ot.common.model.securityDialog.xact.sector.XactSectorDao
import com.vsg.ot.common.model.securityDialog.xact.sector.XactSectorViewModel
import com.vsg.ot.ui.activities.master.MasterBatchActivity
import com.vsg.ot.ui.activities.securityDialog.xact.util.FilterTypeActivityXactSector
import com.vsg.ot.ui.common.securityDigital.xact.sector.UICRUDXactSector
import com.vsg.ot.ui.data.DataBaseActivity

@ExperimentalStdlibApi
class XactSectorActivity :
    CurrentBaseActivityPagingGeneric<XactSectorActivity, XactSectorViewModel,
            XactSectorDao, XactSector,
            FilterTypeActivityXactSector, UICRUDXactSector<XactSectorActivity>>(
        XactSectorViewModel::class.java,
        FilterTypeActivityXactSector::class.java
    ) {

    override fun oSetStringTitleForActionBar(): Int = R.string.ActivityMainCommandSector
    override fun aFinishExecute() {
        onEventMakeFilter = { item, find, it ->
            val filter: PagingData<XactSector> =
                when (item) {
                    FilterTypeActivityXactSector.NAME -> it.filter { s ->
                        s.valueCode.contains(
                            find,
                            true
                        )
                    }
                    else -> it
                }
            filter
        }
        onEventSetCRUDForApply = { context, operation -> UICRUDXactSector(context, operation) }


    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.MenuActionXactUpdateSector -> {
                loadActivity(DataBaseActivity::class.java)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}