package com.vsg.ot.ui.activities.setting

import androidx.paging.PagingData
import androidx.paging.filter
import com.vsg.helper.ui.util.CurrentBaseActivityPagingGeneric
import com.vsg.ot.R
import com.vsg.ot.common.model.setting.profile.SettingProfile
import com.vsg.ot.common.model.setting.profile.SettingProfileDao
import com.vsg.ot.common.model.setting.profile.SettingProfileViewModel
import com.vsg.ot.ui.activities.setting.util.FilterTypeActivitySettingProfile
import com.vsg.ot.ui.common.setting.UICRUDSettingProfile

@ExperimentalStdlibApi
class SettingProfileActivity :
    CurrentBaseActivityPagingGeneric<SettingProfileActivity, SettingProfileViewModel,
            SettingProfileDao, SettingProfile,
            FilterTypeActivitySettingProfile, UICRUDSettingProfile<SettingProfileActivity>>(
        SettingProfileViewModel::class.java,
        FilterTypeActivitySettingProfile::class.java
    ) {

    override fun oSetStringTitleForActionBar(): Int = R.string.ActivityMainCommandSettingProfile
    override fun aFinishExecute() {
        onEventMakeFilter = { item, find, it ->
            val filter: PagingData<SettingProfile> =
                when (item) {
                    FilterTypeActivitySettingProfile.NAME -> it.filter { s ->
                        s.description.contains(
                            find,
                            true
                        )
                    }
                    else -> it
                }
            filter
        }
        onEventSetCRUDForApply = { context, operation -> UICRUDSettingProfile(context, operation) }
    }
}