package com.vsg.ot.common.model.setting.profile.menu

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.vsg.helper.common.model.EntityForeignKeyID
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import com.vsg.ot.common.model.setting.menu.SettingMenu
import com.vsg.ot.common.model.setting.profile.SettingProfile
import common.model.init.entity.EntityOt

@Entity(tableName = SettingProfileMenu.ENTITY_NAME)
class SettingProfileMenu : EntityOt<SettingProfileMenu>() {

    //region properties
    @EntityForeignKeyID(10)
    @ColumnInfo(index = true)
    var idProfile: Int = 0

    @EntityForeignKeyID(10)
    @Ignore
    var profile: SettingProfile? = null

    @EntityForeignKeyID(20)
    @ColumnInfo(index = true)
    var idMenu: Int = 0

    @EntityForeignKeyID(20)
    @Ignore
    var menu: SettingMenu? = null
    //endregion

    //region methods
    @Ignore
    @DrawableRes
    override fun oGetDrawablePicture(): Int = R.drawable.pic_setting_profile_menu

    override fun oGetSpannedGeneric(): StringBuilder =
        StringBuilder().toTitleSpanned(description)

    override fun aEquals(other: Any?): Boolean {
        if (other !is SettingProfileMenu) return false
        return valueCode == other.valueCode
                && description == other.description
                && createDate.time == other.createDate.time
                && idProfile == other.idProfile
                && idMenu == other.idMenu
    }

    companion object {
        const val ENTITY_NAME = "masterSettingProfileMenu"
    }
}