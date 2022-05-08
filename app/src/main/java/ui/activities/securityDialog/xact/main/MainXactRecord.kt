package com.vsg.ot.ui.activities.securityDialog.xact.main

import android.widget.ScrollView
import com.vsg.helper.ui.util.BaseActivity
import com.vsg.ot.R
import com.vsg.ot.ui.activities.securityDialog.xact.XactEventActivity
import com.vsg.ot.ui.activities.securityDialog.xact.XactRecordActivity
import com.vsg.ot.ui.activities.securityDialog.xact.XactSectorActivity

@ExperimentalStdlibApi
class MainXactRecord : BaseActivity(R.layout.sub_main_xact) {

    init {
        onEventMakeActivityForSelector = {
            val view: ScrollView = it.apply {
                add(
                    R.drawable.pic_xact_record,
                    getString(R.string.ActivityXactRecordText),
                    dataClass = XactRecordActivity::class.java
                )
                add(
                    R.drawable.pic_xact_sector,
                    getString(R.string.ActivityXactSectorText),
                    dataClass = XactSectorActivity::class.java
                )
                addWithAction(
                    R.drawable.pic_xact_event,
                    getString(R.string.ActivityXactProcessText),
                    dataClass = XactEventActivity::class.java
                )
            }.makeLayout()
            view
        }
    }

    override fun onExecuteCreate() {
        setActivityAsSelector()
    }
}