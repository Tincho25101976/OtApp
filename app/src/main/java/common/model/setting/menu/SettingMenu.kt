package com.vsg.ot.common.model.setting.menu

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.Ignore
import com.vsg.helper.helper.string.HelperString.Static.toTitleSpanned
import com.vsg.ot.R
import com.vsg.ot.common.model.init.entity.EntityOtWithPicture

@Entity(tableName = SettingMenu.ENTITY_NAME)
class SettingMenu : EntityOtWithPicture<SettingMenu>() {

    //region methods
    @Ignore
    @DrawableRes
    override fun oGetDrawablePicture(): Int = R.drawable.pic_setting_menu

    override fun oGetSpannedGeneric(): StringBuilder =
        StringBuilder().toTitleSpanned(description)

    override fun aEquals(other: Any?): Boolean {
        if (other !is SettingMenu) return false
        return valueCode == other.valueCode
                && description == other.description
                && createDate.time == other.createDate.time
                && isEnabled == other.isEnabled
    }

    //endregion
    companion object {
        const val ENTITY_NAME = "masterSettingMenu"
    }
}