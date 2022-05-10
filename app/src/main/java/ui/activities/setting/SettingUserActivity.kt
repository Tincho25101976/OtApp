package com.vsg.ot.ui.activities.setting

import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.helper.ui.util.CurrentBaseActivityPagingGeneric
import com.vsg.ot.R
import com.vsg.ot.common.model.setting.user.SettingUser
import com.vsg.ot.common.model.setting.user.SettingUserDao
import com.vsg.ot.common.model.setting.user.SettingUserViewModel
import com.vsg.ot.ui.activities.setting.util.FilterTypeActivitySettingUser
import com.vsg.ot.ui.common.setting.UICRUDSettingUser

@ExperimentalStdlibApi
class SettingUserActivity:
    CurrentBaseActivityPagingGeneric<SettingUserActivity, SettingUserViewModel,
            SettingUserDao, SettingUser,
            FilterTypeActivitySettingUser, UICRUDSettingUser<SettingUserActivity>>(
        SettingUserViewModel::class.java,
        FilterTypeActivitySettingUser::class.java
    ) {

    override fun oSetStringTitleForActionBar(): Int = R.string.ActivityMainCommandSettingUser
    override fun aFinishExecute() {
        onEventMakeFilter = { item, find, it ->
            val filter: PagingData<SettingUser> =
                when (item) {
                    FilterTypeActivitySettingUser.NAME -> it.filter { s ->
                        s.name.contains(
                            find,
                            true
                        )
                    }
                    else -> it
                }
            filter
        }
        onEventSetCRUDForApply = { context, operation -> UICRUDSettingUser(context, operation) }
    }
}