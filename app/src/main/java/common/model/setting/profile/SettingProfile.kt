package com.vsg.ot.common.model.setting.profile

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.Ignore
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import com.vsg.ot.common.model.setting.profile.menu.SettingProfileMenu
import com.vsg.ot.common.model.setting.profile.user.SettingProfileUser
import com.vsg.ot.common.model.setting.user.SettingUser
import common.model.init.entity.EntityOt

@Entity(tableName = SettingProfile.ENTITY_NAME)
class SettingProfile : EntityOt<SettingProfile>() {

    //region properties
    var users: List<SettingProfileUser> = mutableListOf()
    var menus: List<SettingProfileMenu> = mutableListOf()
    //endregion

    //region methods
    @Ignore
    @DrawableRes
    override fun oGetDrawablePicture(): Int = R.drawable.pic_setting_profile

    override fun oGetSpannedGeneric(): StringBuilder =
        StringBuilder().toTitleSpanned(description)

    override fun aEquals(other: Any?): Boolean {
        if (other !is SettingUser) return false
        return valueCode == other.valueCode
                && description == other.description
                && createDate.time == other.createDate.time
    }
    //endregion

    companion object {
        const val ENTITY_NAME = "masterSettingProfile"
    }
}