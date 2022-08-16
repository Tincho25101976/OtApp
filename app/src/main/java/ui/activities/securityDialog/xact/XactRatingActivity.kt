package com.vsg.ot.ui.activities.securityDialog.xact

import android.view.Menu
import android.view.MenuItem
import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.helper.ui.util.CurrentBaseActivityPagingGenericParseExport
import com.vsg.ot.R
import com.vsg.ot.common.model.securityDialog.xact.rating.XactRating
import com.vsg.ot.common.model.securityDialog.xact.rating.XactRatingDao
import com.vsg.ot.common.model.securityDialog.xact.rating.XactRatingViewModel
import com.vsg.ot.ui.activities.securityDialog.xact.util.FilterTypeActivityXactRating
import com.vsg.ot.ui.common.securityDigital.xact.rating.UICRUDXactRating
import com.vsg.ot.ui.common.securityDigital.xact.rating.UIUpdateDataXactRating

@ExperimentalStdlibApi
class XactRatingActivity :
    CurrentBaseActivityPagingGenericParseExport<XactRatingActivity, XactRatingViewModel,
            XactRatingDao, XactRating,
            FilterTypeActivityXactRating,
            UICRUDXactRating<XactRatingActivity>>(
        XactRatingViewModel::class.java,
        FilterTypeActivityXactRating::class.java
    ) {

    override fun oSetStringTitleForActionBar(): Int = R.string.ActivityMainCommandRating
    override fun aFinishExecute() {
        onEventMakeFilter = { item, find, it ->
            val filter: PagingData<XactRating> =
                when (item) {
                    FilterTypeActivityXactRating.NAME -> it.filter { s ->
                        s.valueCode.contains(
                            find,
                            true
                        )
                    }
                    else -> it
                }
            filter
        }
        onEventSetCRUDForApply = { context, operation -> UICRUDXactRating(context, operation) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_update_source, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.MenuActionUpdateSource -> loadActivity(UIUpdateDataXactRating::class.java)
        }
        return super.onOptionsItemSelected(item)
    }
}