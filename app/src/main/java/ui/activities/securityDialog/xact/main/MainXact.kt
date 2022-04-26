package com.vsg.ot.ui.activities.securityDialog.xact.main

import android.widget.ScrollView
import com.vsg.helper.ui.util.BaseActivity
import com.vsg.ot.R
import com.vsg.ot.ui.activities.master.MasterBatchActivity
import com.vsg.ot.ui.activities.master.MasterItemActivity
import ui.activities.securityDialog.xact.XactActivity

@ExperimentalStdlibApi
class MainXact : BaseActivity(R.layout.sub_main_xact) {

    init {
        onEventMakeActivityForSelector = {
            val view: ScrollView = it.apply {
                add(
                    R.drawable.pic_xact,
                    getString(R.string.ActivityXactText),
                    dataClass = XactActivity::class.java
                )
                add(
                    R.drawable.pic_sector,
                    getString(R.string.ActivityXactSectorText),
                    dataClass = MasterItemActivity::class.java
                )
                addWithAction(
                    R.drawable.pic_process,
                    getString(R.string.ActivityXactProcessText),
                    dataClass = MasterBatchActivity::class.java
                )
            }.makeLayout()
            view
        }
    }

    override fun onExecuteCreate() {
        setActivityAsSelector()
    }
}