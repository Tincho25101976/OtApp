package com.vsg.ot.ui.activities.setting

import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.helper.ui.util.CurrentBaseActivityPagingGeneric
import com.vsg.ot.R
import com.vsg.ot.common.model.setting.menu.SettingMenu
import com.vsg.ot.common.model.setting.menu.SettingMenuDao
import com.vsg.ot.common.model.setting.menu.SettingMenuViewModel
import com.vsg.ot.ui.activities.setting.util.FilterTypeActivitySettingMenu
import com.vsg.ot.ui.common.setting.UICRUDSettingMenu

@ExperimentalStdlibApi
class SettingMenuActivity :
    CurrentBaseActivityPagingGeneric<SettingMenuActivity, SettingMenuViewModel,
            SettingMenuDao, SettingMenu,
            FilterTypeActivitySettingMenu, UICRUDSettingMenu<SettingMenuActivity>>(
        SettingMenuViewModel::class.java,
        FilterTypeActivitySettingMenu::class.java
    ) {

    override fun oSetStringTitleForActionBar(): Int = R.string.ActivityMainCommandSettingMenu
    override fun aFinishExecute() {
        onEventMakeFilter = { item, find, it ->
            val filter: PagingData<SettingMenu> =
                when (item) {
                    FilterTypeActivitySettingMenu.NAME -> it.filter { s ->
                        s.description.contains(
                            find,
                            true
                        )
                    }
                    else -> it
                }
            filter
        }
        onEventSetCRUDForApply = { context, operation -> UICRUDSettingMenu(context, operation) }
    }
}