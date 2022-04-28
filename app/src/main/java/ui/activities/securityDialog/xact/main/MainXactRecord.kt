package com.vsg.ot.ui.activities.securityDialog.xact.main

import android.widget.ScrollView
import com.vsg.helper.ui.util.BaseActivity
import com.vsg.ot.R
import com.vsg.ot.ui.activities.securityDialog.xact.XactProcessActivity
import com.vsg.ot.ui.activities.securityDialog.xact.XactSectorActivity
import ui.activities.securityDialog.xact.XactRecordActivity

@ExperimentalStdlibApi
class MainXactRecord : BaseActivity(R.layout.sub_main_xact) {

    init {
        onEventMakeActivityForSelector = {
            val view: ScrollView = it.apply {
                add(
                    R.drawable.pic_xact,
                    getString(R.string.ActivityXactText),
                    dataClass = XactRecordActivity::class.java
                )
                add(
                    R.drawable.pic_sector,
                    getString(R.string.ActivityXactSectorText),
                    dataClass = XactSectorActivity::class.java
                )
                addWithAction(
                    R.drawable.pic_process,
                    getString(R.string.ActivityXactProcessText),
                    dataClass = XactProcessActivity::class.java
                )
            }.makeLayout()
            view
        }
    }

    override fun onExecuteCreate() {
        setActivityAsSelector()
    }
}