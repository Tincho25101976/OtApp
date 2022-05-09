package com.vsg.ot.ui.activities.setting.main

import android.widget.ScrollView
import com.vsg.helper.ui.util.BaseActivity
import com.vsg.ot.R
import com.vsg.ot.ui.activities.securityDialog.xact.main.MainXactRecord

@ExperimentalStdlibApi
class MainSetting : BaseActivity(R.layout.main_setting) {
    init {
        onEventMakeActivityForSelector = {
            val view: ScrollView = it.apply {
                add(
                    R.drawable.pic_setting_user,
                    getString(R.string.ActivityMainCommandRecord),
                    dataClass = MainXactRecord::class.java
                )
                add(
                    R.drawable.pic_security_dialog,
                    getString(R.string.ActivityMainCommandSecurityDialog),
                    dataClass = MainXactRecord::class.java
                )
            }.makeLayout()
            view
        }
    }

    override fun onExecuteCreate() {
        setActivityAsSelector()
    }
}