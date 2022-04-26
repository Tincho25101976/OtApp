package com.vsg.ot.ui.activities.securityDialog.main

import android.widget.ScrollView
import com.vsg.helper.ui.util.BaseActivity
import com.vsg.ot.R
import com.vsg.ot.ui.activities.securityDialog.xact.main.MainXact

@ExperimentalStdlibApi
class MainSecurityDialog : BaseActivity(R.layout.main_security_dialog) {
    init {
        onEventMakeActivityForSelector = {
            val view: ScrollView = it.apply {
                add(
                    R.drawable.pic_xact,
                    getString(R.string.ActivityMainCommandRecord),
                    dataClass = MainXact::class.java
                )
                add(
                    R.drawable.pic_security_dialog,
                    getString(R.string.ActivityMainCommandSecurityDialog),
                    dataClass = MainXact::class.java
                )
            }.makeLayout()
            view
        }
    }

    override fun onExecuteCreate() {
        setActivityAsSelector()
    }
}