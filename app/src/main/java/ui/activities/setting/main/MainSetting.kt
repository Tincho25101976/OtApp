package com.vsg.ot.ui.activities.setting.main

import android.widget.ScrollView
import com.vsg.helper.ui.util.BaseActivity
import com.vsg.ot.R
import com.vsg.ot.ui.activities.securityDialog.xact.main.MainXactRecord
import com.vsg.ot.ui.activities.setting.SettingMenuActivity
import com.vsg.ot.ui.activities.setting.SettingUserActivity

@ExperimentalStdlibApi
class MainSetting : BaseActivity(R.layout.main_setting) {
    init {
        onEventMakeActivityForSelector = {
            val view: ScrollView = it.apply {
                add(
                    R.drawable.pic_setting_user,
                    getString(R.string.ActivityMainCommandSettingUser),
                    dataClass = SettingUserActivity::class.java
                )
                add(
                    R.drawable.pic_setting_menu,
                    getString(R.string.ActivityMainCommandSettingMenu),
                    dataClass = SettingMenuActivity::class.java
                )
                add(
                    R.drawable.pic_setting_profile,
                    getString(R.string.ActivityMainCommandSettingProfile),
                    dataClass = MainXactRecord::class.java
                )
                add(
                    R.drawable.pic_setting_profile_menu,
                    getString(R.string.ActivityMainCommandSettingProfileMenu),
                    dataClass = MainXactRecord::class.java
                )
                add(
                    R.drawable.pic_setting_profile_user,
                    getString(R.string.ActivityMainCommandSettingProfileUser),
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